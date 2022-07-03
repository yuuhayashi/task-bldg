package osm.surveyor.task;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
