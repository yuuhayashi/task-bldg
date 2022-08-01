package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import osm.surveyor.task.city.model.Task;

public interface TaskRepository extends JpaRepository<Task,String> {
	
	@Query("SELECT t FROM Task t WHERE t.citycode = :citycode AND t.meshcode = :meshcode order by update_time")
    List<Task> serchByMesh(@Param("citycode")String citycode,@Param("meshcode")String meshcode);

}
