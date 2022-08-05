package osm.surveyor.task.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Operation;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.city.model.Task;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
public class TaskService {

	@Autowired
	TaskRepository repository;
	
	@Autowired
	CitymeshRepository meshRepository;

	public void add(Task task, UserDetails user) {
		if (task.getOperation() == Operation.RESERVE) {
			task.setStatus(Status.RESERVED);
		}
		else if (task.getOperation() == Operation.CANCEL) {
			task.setStatus(Status.ACCEPTING);
		}
		else if (task.getOperation() == Operation.DONE) {
			task.setStatus(Status.IMPORTED);
		}
		else {
			NotAcceptableException e = new NotAcceptableException("未サポートのオペレーションです: "+ task.getOperation());
			e.setTask(task);
			throw e;
		}
		
		String uuid = UUID.randomUUID().toString();

		CitymeshPK pk = new CitymeshPK();
		pk.setCitycode(task.getCitycode());
		pk.setMeshcode(task.getMeshcode());
		Citymesh mesh = meshRepository.getById(pk);
		
		Task ctask = getTaskByMesh(task.getCitycode(), task.getMeshcode());
		if (ctask == null) {
			task.setPreId(uuid);
			task.setCurrentId(uuid);
		}
		else {
			if (!ctask.getCurrentId().equals(task.getCurrentId())) {
				ConflictException e = new ConflictException("他のスレッドによってタスクが変更されたため更新できませんでした。");
				e.setTask(task);
				throw e;
			}
			if (task.getOperation() == Operation.RESERVE) {
				if (ctask.getStatus() != Status.ACCEPTING) {
					NotAcceptableException e = new NotAcceptableException("ステータスがACCEPTIONGではないためタスク登録できませんでした : "+ task.getOperation());
					e.setTask(task);
					throw e;
				}
			}
			else if (task.getOperation() == Operation.CANCEL) {
				if (ctask.getStatus() != Status.RESERVED) {
					NotAcceptableException e = new NotAcceptableException("ステータスがRESERVEDではないためタスク登録できませんでした : "+ task.getOperation());
					e.setTask(task);
					throw e;
				}
				if (!ctask.getUsername().equals(user.getUsername())) {
					NotAcceptableException e = new NotAcceptableException("他のマッパーのタスクはCANCELできません");
					e.setTask(task);
					throw e;
				}
			}
			else if (task.getOperation() == Operation.DONE) {
				if (ctask.getStatus() != Status.RESERVED) {
					NotAcceptableException e = new NotAcceptableException("タスク登録されていないためタスク完了できませんでした");
					e.setTask(task);
					throw e;
				}
				if (!ctask.getUsername().equals(user.getUsername())) {
					NotAcceptableException e = new NotAcceptableException("他のマッパーのタスクは完了できません");
					e.setTask(task);
					throw e;
				}
			}
			task.setPreId(ctask.getCurrentId());
			task.setCurrentId(uuid);
		}
		
		task.setUpdateTime(new Date());
		mesh.setStatus(task.getStatus());
		
		// データベースに格納
		repository.save(task);
		meshRepository.save(mesh);
	}
	
	public Task getTaskByMesh(String citycode, String meshcode) {
		List<Task> tasks = repository.serchByMesh(citycode, meshcode);
		for (Task t : tasks) {
			return t;
		}
		return null;
	}

	public List<Task> getTasks() {
		return repository.findAll();
	}

}
