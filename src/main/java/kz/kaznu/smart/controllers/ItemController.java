package kz.kaznu.smart.controllers;

import javassist.compiler.SymbolTable;
import kz.kaznu.smart.models.dto.ItemInfo;
import kz.kaznu.smart.models.dto.ItemParamsDto;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.ItemParam;
import kz.kaznu.smart.models.entities.Photo;
import kz.kaznu.smart.models.enums.*;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.ParamsService;
import kz.kaznu.smart.services.PhotoService;
import kz.kaznu.smart.services.UserService;
import kz.kaznu.smart.utils.ColorUtils;
import kz.kaznu.smart.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    private final PhotoService photoService;
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
        } else {
            List<Color> colors = paramsService.getItemParamValuesByItem(foundItem.getName(), Params.COLOR)
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin/items")
    public String get(Model model) {
        List<Item> items = itemService.getAll();
        model.addAttribute("items", items);
        return "admin/items";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin/items/{id}")
    public String admin(@PathVariable(name = "id") Long id, Model model) {
        Optional<Item> opItem = itemService.getItemById(id);
        if (opItem.isPresent()) {
            List<String> photos = new ArrayList<>();
            File file = new File("photos/");
            if (file.isDirectory()) {
                photos = Arrays.asList(Objects.requireNonNull(file.list()));
            }
            Item item = opItem.get();
            model.addAttribute("photos", item.getPhotos());
            model.addAttribute("params", paramsService.getItemParamsByItem(item));
            model.addAttribute("itemInfo", Utils.item2ItemInfo(item));
            model.addAttribute("brands", Brand.values());
            model.addAttribute("types", ItemType.values());
            model.addAttribute("listPhoto", photos);
            model.addAttribute("itemParams", Params.values());
            return "admin/itemPage";
        }
        return "redirect:/admin/items";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin/newItem")
    public String newItem(Model model) {
        model.addAttribute("itemInfo", new ItemInfo());
        model.addAttribute("brands", Brand.values());
        model.addAttribute("types", ItemType.values());
        model.addAttribute("itemParams", Params.values());
        return "admin/itemPage";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/items")
    public String update(@ModelAttribute("itemInfo") ItemInfo itemInfo) {
        Item item = itemService.save(itemInfo);
        return "redirect:/admin/items/" + item.getId() + "?success";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/admin/items")
    public String delete(@RequestParam(name = "id") Long id) {
        Optional<Item> optionalItem = itemService.getItemById(id);
        optionalItem.ifPresent(itemService::delete);
        return "redirect:/admin/items";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/items/photo")
    public String addPhoto(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "photo") String photo,
                           @RequestParam(name = "main") Integer main) {

        photo = photo.replace(".jpg", "");
        Optional<Item> opItem = itemService.getItemById(id);
        if (opItem.isPresent()) {
            Item item = opItem.get();
            List<Photo> photos = item.getPhotos();
            List<String> photoNames = photos.stream().map(Photo::getPhotoName).collect(Collectors.toList());

            if (!photoNames.contains(photo)) {
                if (main == 1) {
                    photos.stream().filter(Photo::isMain).forEach(p -> {
                        p.setMain(false);
                        photoService.save(p);
                    });
                }
                Photo newPhoto = Photo.builder()
                        .item(item)
                        .photoName(photo)
                        .main(main == 1)
                        .build();
                photoService.save(newPhoto);
            }
        }
        return "redirect:/admin/items/" + id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/admin/items/photo")
    public String deletePhoto(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "photo") String photo) {
        Item item = itemService.getItemById(id).orElse(null);
        Optional<Photo> optionalPhoto = photoService.getByItemAndPhotoName(item, photo);
        optionalPhoto.ifPresent(photoService::delete);
        return "redirect:/admin/items/" + id;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/admin/items/photo")
    public String patch(@RequestParam(name = "id") Long id,
                        @RequestParam(name = "photo") String photo,
                        @RequestParam(name = "action") String action) {
        Optional<Item> itemOptional = itemService.getItemById(id);
        itemOptional.ifPresent(item -> {
            boolean main = action.equals("main");

            if (main) {
                item.getPhotos().stream().filter(Photo::isMain).forEach(p -> {
                    p.setMain(false);
                    photoService.save(p);
                });
            }

            item.getPhotos().stream().filter(ph -> ph.getPhotoName().equals(photo)).forEach(i -> {
                i.setMain(main);
                photoService.save(i);
            });
        });
        return "redirect:/admin/items/" + id;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/items/params")
    public String addPhoto(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "param") String param,
                           @RequestParam(name = "value") String value,
                           @RequestParam(name = "order") Integer order) {

        try {
            Params params = Params.valueOf(param);

            Optional<Item> item = itemService.getItemById(id);
            item.ifPresent(i -> {
                paramsService.save(i, params, value, order);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/items/" + id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/admin/items/params")
    public String deleteParams(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "paramId") Long paramId) {

        Optional<ItemParam> param = paramsService.getById(paramId);
        param.ifPresent(paramsService::delete);
        return "redirect:/admin/items/" + id;
    }
}
