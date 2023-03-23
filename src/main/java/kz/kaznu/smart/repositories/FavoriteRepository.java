package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Favorite;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f.item from Favorite f where f.user = ?1")
    List<Item> getFavoritesByUser(User user);

    @Query("select f from Favorite f where f.user = :user and f.item = :item")
    Optional<Favorite> getByUserAndItem(User user, Item item);
}
