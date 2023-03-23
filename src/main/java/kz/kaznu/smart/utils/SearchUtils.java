package kz.kaznu.smart.utils;

import kz.kaznu.smart.models.enums.Brand;
import kz.kaznu.smart.models.enums.Color;
import kz.kaznu.smart.models.enums.Memory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchUtils {

    public static List<String> getColorByName(String name) {
        if (name.equals("all"))
            return Arrays.stream(Color.values()).map(Color::getName).collect(Collectors.toList());
        return Collections.singletonList(name);
    }

    public static List<String> getMemoryByName(String name) {
        if (name.equals("all"))
            return Arrays.stream(Memory.values()).map(Memory::getName).collect(Collectors.toList());
        return Collections.singletonList(name);
    }

    public static List<Brand> getBrandByName(String name) {
        if (name.equals("all"))
            return Arrays.stream(Brand.values()).collect(Collectors.toList());
        return Collections.singletonList(Brand.valueOf(name));
    }
}
