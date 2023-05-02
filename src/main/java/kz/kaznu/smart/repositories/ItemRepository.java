package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.enums.Brand;
import kz.kaznu.smart.models.enums.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.name like ?1 and i.quantity > 0")
    List<Item> getItemsByName(String name);

    @Query("select i from Item i where upper(i.fullName) like upper(?1)")
    Optional<Item> getItemByFullName(String name);

    @Query("select i from Item i where i.id = ?1")
    Optional<Item> getItemById(Long id);

    @Query("select i from Item i")
    List<Item> getItems(Pageable pageable);

    @Query("select i from Item i where i.itemType = ?1 and i.quantity > 0")
    Page<Item> getItemsByType(ItemType type, Pageable pageable);

    @Query("select i from Item i join ItemParam ip on i = ip.item " +
            "where i.newPrice > :price1 " +
            "and i.itemType = :type " +
            "and i.newPrice < :price2 " +
            "and i.brand in (:brands) " +
            "and (ip.param.name = 'COLOR' and ip.value in (:colors)) and i.quantity > 0")
    Page<Item> search(ItemType type ,Integer price1, Integer price2, List<Brand> brands, List<String> colors, Pageable pageable);

    @Query("select distinct(i) from Item i join ItemParam ip on i = ip.item " +
            "where lower(i.fullName) like %:text% " +
            "or lower(i.brand) like %:text% " +
            "or lower(i.itemType) like %:text% " +
            "or (ip.param.name = 'COLOR' and lower(ip.value) like %:text%) and i.quantity > 0")
    Page<Item> search(String text, Pageable pageable);
}
