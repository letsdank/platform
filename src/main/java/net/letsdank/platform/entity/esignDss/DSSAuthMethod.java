package net.letsdank.platform.entity.esignDss;

import lombok.Getter;

@Getter
public enum DSSAuthMethod {
    // Первичный
    PRIMARY_2FA("Первичный двуфакторная"),
    PRIMARY_AUTHCODE("Первичный код авторизации"),
    PRIMARY_AUTHCERT("Первичный сертификат авторизации"),
    PRIMARY_AUTHDATA("Первичный учетные данные"),

    // Вторичный
    SECONDARY_AIRKEYLITE("Вторичный air key lite"),
    SECONDARY_DSSSDK("Вторичный DSS SDK"),
    SECONDARY_MOBILEAPP("Вторичный мобильное приложение"),
    SECONDARY_OFFLINE("Вторичный офлайн подтверждение"),
    SECONDARY_SMS("Вторичный СМС"),
    SECONDARY_EMAIL("Вторичный электронная почта");

    private final String name;

    DSSAuthMethod(String name) {
        this.name = name;
    }
}
