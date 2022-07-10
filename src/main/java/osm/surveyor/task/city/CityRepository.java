package osm.surveyor.task.city;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.city.model.City;

public interface CityRepository extends JpaRepository<City,Long> {
}
