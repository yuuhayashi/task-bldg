package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;

public interface CitymeshRepository extends JpaRepository<Citymesh,CitymeshPK> {

	List<Citymesh> findByCitycode(String citycode);
}
