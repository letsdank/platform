package net.letsdank.platform.entity.finance;

import lombok.Getter;

@Getter
public enum ExchangeRateSettingMethod {
    INTERNET("Загрузка из интернета"),
    MANUALLY("Ручной ввод"),
    MARKUP("Наценка на курс другой валюты"),
    FORMULA("Расчет по формуле");

    private final String displayName;

    ExchangeRateSettingMethod(String displayName) {
        this.displayName = displayName;
    }

}
