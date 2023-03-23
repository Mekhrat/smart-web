package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.enums.Params;

import java.util.List;
import java.util.Map;

public interface ParamsService {

    List<String> getItemParamValuesByItem(Item item, Params param);
    List<String> getItemParamValuesByItem(String itemName, Params param);
    Map<String, String> getParamsByItem(Item item);
    Map<String, List<ItemParamsDto>> getAllSortedItemParams(Item item);
    List<ItemParamsDto> getMainParamsByItem(Item item);
}
