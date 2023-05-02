package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.ItemParam;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.Params;
import kz.kaznu.smart.repositories.ItemParamRepository;
import kz.kaznu.smart.repositories.ItemRepository;
import kz.kaznu.smart.repositories.ParamRepository;
import kz.kaznu.smart.services.ParamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParamsServiceImpl implements ParamsService {
    private final ItemRepository itemRepository;
    private final ItemParamRepository paramRepository;
    private final ParamRepository paramsRepository;

    @Override
    public List<String> getItemParamValuesByItem(Item item, Params param) {
        List<Item> items = itemRepository.getItemsByName(item.getName());
        Params filter = param.equals(Params.COLOR) ? Params.MEMORY : Params.COLOR;
        return items.stream().filter(i ->
                        paramRepository.getParamValueByItem(i, filter).equals(paramRepository.getParamValueByItem(item, filter)))
                .map(i -> {return paramRepository.getParamValueByItem(i, param);
                }).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getItemParamValuesByItem(String itemName, Params param) {
        List<Item> items = itemRepository.getItemsByName(itemName);
        return items.stream().map(i -> paramRepository.getParamValueByItem(i, param)).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getParamsByItem(Item item) {
        List<ItemParam> allParams = paramRepository.getAllParamsByItem(item);
        return allParams.stream().collect(
                Collectors.toMap(k -> k.getParam().getName().name(), ItemParam::getValue)
        );
    }

    @Override
    public Map<String, List<ItemParamsDto>> getAllSortedItemParams(Item item) {
        List<ItemParam> allParams = paramRepository.getAllParamsByItem(item);
        Map<String, List<ItemParamsDto>> params = new HashMap<>();

        allParams.forEach(i -> {
            List<ItemParamsDto> values = params.get(i.getParam().getType().getName());
            if (values == null) {
                params.put(i.getParam().getType().getName(), new ArrayList<>(Arrays.asList(new ItemParamsDto(i.getParam().getName().getName(), i.getValue()))));
            }
            else {
                values.add(new ItemParamsDto(i.getParam().getName().getName(), i.getValue()));
            }
        });
        return params;
    }

    @Override
    public Optional<ItemParam> getById(Long paramId) {
        return paramRepository.getFirstById(paramId);
    }

    @Override
    public List<ItemParamsDto> getMainParamsByItem(Item item) {
        return paramRepository.getItemParamsByOrderValue(item, Collections.singletonList(0)).stream()
                .map( i -> new ItemParamsDto(i.getParam().getName().getName(), i.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemParam> getItemParamsByItem(Item item) {
        return paramRepository.getAllParamsByItem(item);
    }

    @Override
    public ItemParam save(Item i, Params param, String value, Integer order) {
        Optional<Param> firstByName = paramsRepository.getFirstByName(param);
        if (firstByName.isPresent()) {
            Param params = firstByName.get();
            boolean ifExist = i.getItemParams().stream().anyMatch(item -> item.getParam().equals(params));
            if (!ifExist) {
                ItemParam itemParam = ItemParam.builder()
                        .param(params)
                        .item(i)
                        .value(value)
                        .orderValue(order)
                        .build();
                return paramRepository.save(itemParam);
            }
        }
        return null;
    }

    @Override
    public void delete(ItemParam param) {
        paramRepository.delete(param);
    }

}


