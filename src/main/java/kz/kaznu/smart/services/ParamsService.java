package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.ItemParam;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.Params;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ParamsService {

    List<String> getItemParamValuesByItem(Item item, Params param);
    List<String> getItemParamValuesByItem(String itemName, Params param);
    Map<String, String> getParamsByItem(Item item);
    Map<String, List<ItemParamsDto>> getAllSortedItemParams(Item item);
    List<ItemParamsDto> getMainParamsByItem(Item item);

    List<ItemParam> getItemParamsByItem(Item item);

    ItemParam save(Item i, Params param, String value, Integer order);

    void delete(ItemParam param);

    Optional<ItemParam> getById(Long paramId);
}
