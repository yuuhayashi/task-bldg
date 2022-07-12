package osm.surveyor.task.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import osm.surveyor.task.user.model.SiteUser;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByUsername(String username);
    boolean existsByUsername(String username);
}
