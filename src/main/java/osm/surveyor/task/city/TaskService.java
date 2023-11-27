package osm.surveyor.task.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Operation;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.city.model.TaskEntity;

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

	public void add(TaskEntity task) {
		if (task.getOperation() == Operation.RESERVE) {
			task.setStatus(Status.RESERVED);
		}
		else if (task.getOperation() == Operation.CANCEL) {
			task.setStatus(Status.PREPARATION);
		}
		else if (task.getOperation() == Operation.NG) {
			task.setStatus(Status.NG);
		}
		else if (task.getOperation() == Operation.OK) {
			task.setStatus(Status.OK);
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
		
		TaskEntity ctask = getTaskByMesh(task.getCitycode(), task.getMeshcode());
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
			if (task.getOperation() == Operation.CANCEL) {
				if (ctask.getStatus() != Status.RESERVED) {
					NotAcceptableException e = new NotAcceptableException("タスクが'編集中'ではないため'編集取消'できませんでした : "+ task.getOperation());
					e.setTask(task);
					throw e;
				}
			}
			else if (task.getOperation() == Operation.OK) {
				// タスク予約していなくてもインポートできる
				// 他のマッパーが予約していてもインポート可能
				String username = task.getUsername();
				if (username == null) {
					TaskException e = new TaskException("編集者が入力されていません");
					e.setTask(task);
					throw e;
				}
				else if (username.isEmpty()) {
					TaskException e = new TaskException("編集者が入力されていません");
					e.setTask(task);
					throw e;
				}
				
				String changeset = task.getChangeSet();
				if (changeset == null) {
					TaskException e = new TaskException("変更セットNoが入力されていません");
					e.setTask(task);
					throw e;
				}
				else if (changeset.isEmpty()) {
					TaskException e = new TaskException("変更セットNoが入力されていません");
					e.setTask(task);
					throw e;
				}
				else {
					try {
						Long.parseLong(changeset);
					}
					catch (NumberFormatException nfe) {
						TaskException e = new TaskException("変更セットNoに数字以外の文字が入っています");
						e.setTask(task);
						throw e;
					}
				}
			}
			else if (task.getOperation() == Operation.NG) {
				String username = task.getUsername();
				if (username == null) {
					TaskException e = new TaskException("編集者が入力されていません");
					e.setTask(task);
					throw e;
				}
				else if (username.isEmpty()) {
					TaskException e = new TaskException("編集者が入力されていません");
					e.setTask(task);
					throw e;
				}

				String comment = task.getComment();
				if (comment == null || comment.isEmpty()) {
					TaskException e = new TaskException("コメントが入力されていません");
					e.setTask(task);
					throw e;
				}
			}
			task.setPreId(ctask.getCurrentId());
			task.setCurrentId(uuid);
		}
		
		task.setUpdateTime(new Date());
		mesh.setStatus(task.getStatus());
		mesh.setUsername(task.getUsername());
		
		// データベースに格納
		repository.save(task);
		meshRepository.save(mesh);
	}
	
	public TaskEntity getTaskByMesh(String citycode, String meshcode) {
		List<TaskEntity> tasks = repository.serchByMesh(citycode, meshcode);
		for (TaskEntity t : tasks) {
			return t;
		}
		return null;
	}

	public List<TaskEntity> getTasks() {
		return repository.findAll();
	}
}
