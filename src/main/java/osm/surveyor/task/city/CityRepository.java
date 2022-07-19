package osm.surveyor.task.city;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.City;

public interface CityRepository extends JpaRepository<City, String> {

	City findByCitycode(String citycode);

	@Transactional
	List<City> deleteByCitycode(String citycode);
}
