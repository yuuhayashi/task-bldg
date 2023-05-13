package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;

public interface CitymeshRepository extends JpaRepository<Citymesh,CitymeshPK> {

	List<Citymesh> findByCitycode(String citycode);
	
	@Query("SELECT m FROM Citymesh m WHERE m.username = :username order by citycode,meshcode")
	List<Citymesh> serchByUser(@Param("username")String username);
}
