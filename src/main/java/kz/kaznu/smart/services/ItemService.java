package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.ItemType;
import kz.kaznu.smart.models.enums.Params;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {

    Optional<Item> getItemById(Long id);
    Optional<Item> getItemByName(String name);
    Page<Item> getTopItemsByType(ItemType type, Integer page, Integer size) ;
    Page<Item> search(ItemType type ,Integer price1, Integer price2, String brand, String color, Integer page, Integer size);
    Page<Item> search(String text, int i, int i1);
}

