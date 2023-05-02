package kz.kaznu.smart.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
    SMARTPHONE("Смартфон"), SMARTWATCH("Смарт сағат"), HEADPHONE("Құлаққап");

    private final String value;
}
