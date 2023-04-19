package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.entities.Favorite;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.repositories.FavoriteRepository;
import kz.kaznu.smart.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Override
    public List<Item> getFavoriteItemsByUser(String userEmail) {
        return favoriteRepository.getFavoritesByUser(userEmail);
    }

    @Override
    public Optional<Favorite> getFavoriteItemsByUserAndItem(String userEmail, Item item) {
        return favoriteRepository.getByUserAndItem(userEmail, item);
    }

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}
