package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.Citymesh;

public interface CitymeshRepository extends JpaRepository<Citymesh,Long> {

	List<Citymesh> findByCitycode(String citycode);
}
