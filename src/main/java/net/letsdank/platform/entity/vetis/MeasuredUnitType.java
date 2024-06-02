package net.letsdank.platform.entity.vetis;

import lombok.Getter;

@Getter
public enum MeasuredUnitType {
    WEIGHT("Вес"),
    VOLUME("Объем"),
    SQUARE("Площадь"),
    LENGTH("Длина"),
    AMOUNT("Количество штук"),
    PACKAGE("Упаковка"),
    POWER("Мощность"),
    ENERGY("Электрический заряд"),
    TIME("Время");

    private final String name;

    MeasuredUnitType(String name) {
        this.name = name;
    }
}
