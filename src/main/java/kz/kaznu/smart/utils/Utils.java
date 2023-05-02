package kz.kaznu.smart.utils;

import kz.kaznu.smart.models.dto.ItemInfo;
import kz.kaznu.smart.models.entities.Item;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<Long> getIdsInString(String str) {
        if (str.equals("")) return new ArrayList<>();
        return Arrays.stream(str.split("s"))
                .filter(a -> !a.equals(""))
                .map(Long::parseLong).collect(Collectors.toList());
    }

    public static String deleteIdInArray(List<Long> ids, Long id) {
        ids.remove(id);
        return ids.stream().map(String::valueOf).collect(Collectors.joining("s"));
    }

    public static Cookie createNewCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60 * 60);
        return cookie;
    }

    public static ItemInfo item2ItemInfo(Item item) {
        return ItemInfo.builder()
                .name(item.getName())
                .fullName(item.getFullName())
                .lastPrice(item.getLastPrice())
                .price(item.getNewPrice())
                .id(item.getId())
                .quantity(item.getQuantity())
                .brand(item.getBrand().name())
                .type(item.getItemType().name())
                .build();
    }
}
