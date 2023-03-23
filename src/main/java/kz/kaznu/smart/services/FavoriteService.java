package kz.kaznu.smart.services;

import kz.kaznu.smart.models.entities.Favorite;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {

    List<Item> getFavoriteItemsByUser(User user);
    Optional<Favorite> getFavoriteItemsByUserAndItem(User user, Item item);
    Favorite save(Favorite favorite);
    void delete(Favorite favorite);

}
