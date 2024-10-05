package osm.surveyor.task.mesh;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import osm.surveyor.task.mesh.model.Citymesh;
import osm.surveyor.task.mesh.model.CitymeshPK;

public interface CitymeshRepository extends JpaRepository<Citymesh,CitymeshPK> {
	
	@Query("SELECT m FROM Citymesh m WHERE m.citycode = :citycode AND m.meshcode = :meshcode")
	Citymesh findOne(@Param("citycode")String citycode, @Param("meshcode")String meshcode);

	List<Citymesh> findByCitycode(String citycode);
	
	@Query("SELECT m FROM Citymesh m WHERE m.username = :username order by citycode,meshcode")
	List<Citymesh> serchByUser(@Param("username")String username);
}
