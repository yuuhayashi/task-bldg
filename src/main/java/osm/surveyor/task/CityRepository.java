package osm.surveyor.task;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.model.City;

public interface CityRepository extends JpaRepository<City,Long> {
}
