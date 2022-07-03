package osm.surveyor.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.model.Task;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final TaskRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		Task task = new Task();
		task.setCitycode("01100");
		task.setMeshcode("000000");
		task.setCityname("北海道 札幌市");
		repository.save(task);

		task = new Task();
		task.setCitycode("07203");
		task.setMeshcode("000000");
		task.setCityname("福島県 郡山市");
		repository.save(task);
	}

}
