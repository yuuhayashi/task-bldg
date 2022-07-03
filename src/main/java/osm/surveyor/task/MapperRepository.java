package osm.surveyor.task;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.model.Mapper;

public interface MapperRepository extends JpaRepository<Mapper,Long> {
}
