package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {

	List<Task> findByCitycode(String citycode);
}
