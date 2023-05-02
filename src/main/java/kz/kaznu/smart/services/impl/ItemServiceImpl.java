package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.dto.ItemInfo;
import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.ItemParam;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.Brand;
import kz.kaznu.smart.models.enums.Color;
import kz.kaznu.smart.models.enums.ItemType;
import kz.kaznu.smart.models.enums.Params;
import kz.kaznu.smart.repositories.ItemParamRepository;
import kz.kaznu.smart.repositories.ItemRepository;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.ParamsService;
import kz.kaznu.smart.utils.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.getItemById(id);
    }

    @Override
    public Optional<Item> getItemByName(String name) {
        return itemRepository.getItemByFullName(name);
    }

    @Override
    public Page<Item> getTopItemsByType(ItemType type, Integer page, Integer size) {
        //todo sort by
        return itemRepository.getItemsByType(type, PageRequest.of(page, size, Sort.by("newPrice").descending()));
    }

    @Override
    public Page<Item> search(ItemType type, Integer price1, Integer price2, String brand, String color, Integer page, Integer size) {
        List<Brand> brands = SearchUtils.getBrandByName(brand);
        List<String> colors = SearchUtils.getColorByName(color);

        return itemRepository.search(type, price1, price2, brands, colors, PageRequest.of(page, size));
    }

    @Override
    public Page<Item> search(String text, int page, int size) {
        return itemRepository.search(text.toLowerCase(Locale.ROOT), PageRequest.of(page, size));
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item save(ItemInfo itemInfo) {
        Item item = itemRepository.getItemById(itemInfo.getId()).orElse(
                Item.builder()
                        .createTime(LocalDateTime.now())
                        .build()
        );
        item.setName(itemInfo.getName());
        item.setFullName(itemInfo.getFullName());
        item.setLastPrice(itemInfo.getLastPrice());
        item.setNewPrice(itemInfo.getPrice());
        item.setUpdateTime(LocalDateTime.now());
        item.setQuantity(itemInfo.getQuantity());
        item.setBrand(Brand.valueOf(itemInfo.getBrand()));
        item.setItemType(ItemType.valueOf(itemInfo.getType()));
        return itemRepository.save(item);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void delete(Item item) {
        itemRepository.delete(item);
    }
}

