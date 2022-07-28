package osm.surveyor.task.city;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Task;

public interface TaskRepository extends JpaRepository<Task,CitymeshPK> {

}
