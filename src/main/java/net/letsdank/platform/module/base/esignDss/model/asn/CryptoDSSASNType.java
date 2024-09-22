package net.letsdank.platform.module.base.esignDss.model.asn;

import lombok.Getter;

@Getter
public enum CryptoDSSASNType {
    PRIMITIVE(0, "Primitive"),
    CONSTRUCTED(32, "Constructed");

    @Getter
    private final int value;
    private final String name;

    CryptoDSSASNType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
