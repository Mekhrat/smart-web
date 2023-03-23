package kz.kaznu.smart.utils;

import kz.kaznu.smart.models.enums.Color;

public class ColorUtils {

    public static String getColorNameByValue(String value) {
        for (Color color: Color.values()) {
            if (color.getName().equals(value))
                return color.name();
        }
        return Color.BLACK.name();
    }
}
