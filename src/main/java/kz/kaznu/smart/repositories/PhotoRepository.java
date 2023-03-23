package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
