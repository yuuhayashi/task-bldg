package osm.surveyor.task.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import osm.surveyor.task.city.model.City;

public interface CityRepository extends JpaRepository<City, String> {

	City findByCitycode(String citycode);

	@Transactional
	List<City> deleteByCitycode(String citycode);
}
