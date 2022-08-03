package osm.surveyor.task.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void add(Task task) {
		if (task.getOperation() == Operation.RESERVE) {
			task.setStatus(Status.RESERVED);
		}
		else {
			throw new RuntimeException("未サポートのオペレーション: "+ task.getOperation());
		}
		
		String uuid = UUID.randomUUID().toString();

		Task ctask = getTaskByMesh(task.getCitycode(), task.getMeshcode());
		if (ctask == null) {
			task.setPreId(uuid);
			task.setCurrentId(uuid);
		}
		else {
			if (!ctask.getCurrentId().equals(task.getCurrentId())) {
				throw new RuntimeException("タスクが変更されたため更新できません");
			}
			if (task.getOperation() == Operation.RESERVE) {
				if (ctask.getStatus() != Status.ACCEPTING) {
					throw new RuntimeException("ACCEPTIONGではないため予約できません: "+ task.getOperation());
				}
			}
			task.setPreId(ctask.getCurrentId());
			task.setCurrentId(uuid);
		}
		
		task.setUpdateTime(new Date());
		
		// データベースに格納
		repository.save(task);
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
