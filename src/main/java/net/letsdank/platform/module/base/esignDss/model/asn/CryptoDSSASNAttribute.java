package net.letsdank.platform.module.base.esignDss.model.asn;

import lombok.Getter;

@Getter
public class CryptoDSSASNAttribute {
    private final String oid;
    private final String name;
    private final Integer length;
    private final String description;
    private final String attribute;
    private final boolean required;

    // Alias: НовыйАтрибут
    public CryptoDSSASNAttribute(String oid, String name, Integer length) {
        this(oid, name, length, "");
    }

    // Alias: НовыйАтрибут
    public CryptoDSSASNAttribute(String oid, String name, Integer length, String description) {
        this(oid, name, length, description, "");
    }

    // Alias: НовыйАтрибут
    public CryptoDSSASNAttribute(String oid, String name, Integer length, String description, String attribute) {
        this(oid, name, length, description, attribute, false);
    }

    // Alias: НовыйАтрибут
    public CryptoDSSASNAttribute(String oid, String name, Integer length, String description, String attribute, boolean required) {
        this.oid = oid;
        this.name = name;
        this.length = length == null ? 0 : length;
        this.description = description;
        this.attribute = attribute;
        this.required = required;
    }


    public String get(String keyField) {
        return switch (keyField) {
            case "oid" -> oid;
            case "name" -> name;
            case "length" -> length.toString();
            case "description" -> description;
            case "attribute" -> attribute;
            case "required" -> Boolean.toString(required);
            default -> "";
        };
    }
}
