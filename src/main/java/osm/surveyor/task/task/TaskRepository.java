package osm.surveyor.task.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import osm.surveyor.task.task.model.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity,String> {
	
	@Query("SELECT t FROM TaskEntity t WHERE t.citycode = :citycode AND t.meshcode = :meshcode order by update_time DESC")
    List<TaskEntity> serchByMesh(@Param("citycode")String citycode, @Param("meshcode")String meshcode);

}
