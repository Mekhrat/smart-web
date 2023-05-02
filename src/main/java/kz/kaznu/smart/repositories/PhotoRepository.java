package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByPhotoName(String photoName);

    Optional<Photo> getFirstByItemAndPhotoName(Item item, String photoName);
}
