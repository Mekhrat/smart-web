package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.*;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.ParamsService;
import kz.kaznu.smart.services.UserService;
import kz.kaznu.smart.utils.ColorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ParamsService paramsService;
    private final UserService userService;

    @GetMapping(value = "/category/{type}/{name}")
    public String getItemByName(@PathVariable(value = "type") String type,
                                @PathVariable(value = "name") String name,
                                Model model) {
        Optional<Item> item = itemService.getItemByName(name);
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        boolean isSmartphone;
        if (item.isEmpty())
            return "redirect:/home";
        try {
            ItemType itemType = ItemType.valueOf(type.toUpperCase());
            isSmartphone = itemType.equals(ItemType.SMARTPHONE);
            Page<Item> items = itemService.getTopItemsByType(itemType, 0, 6);
            model.addAttribute("items", items);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
        Item foundItem = item.get();
        model.addAttribute("item", foundItem);

        fillModelAttributes(model, foundItem, isSmartphone);
        return "itempage";
    }

    private void fillModelAttributes(Model model, Item foundItem, boolean isSmartphone) {
        if (isSmartphone) {
            List<Color> colors = paramsService.getItemParamValuesByItem(foundItem, Params.COLOR)
                    .stream().map(i -> {
                        return Color.valueOf(ColorUtils.getColorNameByValue(i));
                    }).collect(Collectors.toList());
            model.addAttribute("colors", colors);

            List<String> memories = paramsService.getItemParamValuesByItem(foundItem, Params.MEMORY);
            model.addAttribute("memories", memories);
        }
        else {
            List<Color> colors =paramsService.getItemParamValuesByItem(foundItem.getName(), Params.COLOR)
                    .stream().map(i -> {
                        return Color.valueOf(ColorUtils.getColorNameByValue(i));
                    }).collect(Collectors.toList());
            model.addAttribute("colors", colors);
        }

        Map<String, List<ItemParamsDto>> allSortedItemParams = paramsService.getAllSortedItemParams(foundItem);
        model.addAttribute("sorted_params", allSortedItemParams);

        List<ItemParamsDto> mainParams = paramsService.getMainParamsByItem(foundItem);
        model.addAttribute("mainParams", mainParams);

        Map<String, String> params = paramsService.getParamsByItem(foundItem);
        model.addAttribute("params", params);
    }


    @GetMapping(value = "/category/{type}")
    public String getItem(@PathVariable(value = "type") String type,
                          @RequestParam(name = "price1", defaultValue = "0") Integer price1,
                          @RequestParam(name = "price2", defaultValue = "1000000") Integer price2,
                          @RequestParam(name = "brand", defaultValue = "all") String brand,
                          @RequestParam(name = "color", defaultValue = "all") String color,
                          @RequestParam(name = "text", defaultValue = "") String text,
                          @RequestParam(name = "page", defaultValue = "0") Integer page, Model model) {
        if (!text.isEmpty()) {
            String encode = URLEncoder.encode(text);
            return "redirect:/search/" + encode;
        }

        try {
            ItemType itemType = ItemType.valueOf(type.toUpperCase());
            Page<Item> items = itemService.search(itemType, price1, price2, brand, color, page, 24);
            model.addAttribute("items", items);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        model.addAttribute("currentPage", page);
        model.addAttribute("colors", Color.values());
        model.addAttribute("memories", Memory.values());
        model.addAttribute("brands", Brand.values());
        return "items";
    }


    @GetMapping(value = "/search/{text}")
    public String searchText(@PathVariable(name = "text") String text,
                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                             Model model) {
        Page<Item> items = itemService.search(text, page, 24);
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("colors", Color.values());
        model.addAttribute("memories", Memory.values());
        model.addAttribute("brands", Brand.values());

        return "search";
    }


    @GetMapping(value = "/admin/items/{id}")
    public String admin(@PathVariable(name = "id") Long id) {
        return "admin/itemPage";
    }
}
