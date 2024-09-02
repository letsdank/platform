package net.letsdank.platform.module.email.model;

public enum EmailMessageType {
    HTML("HTML"),
    HTML_WITH_IMAGES("HTML"),
    FORMATTED_TEXT("RichText"),
    PLAIN_TEXT("");

    private final String oldType;

    EmailMessageType(String oldType) {
        this.oldType = oldType;
    }

    public static EmailMessageType fromType(String type) {
        if (type == null || type.isEmpty()) {
            return PLAIN_TEXT;
        }

        for (EmailMessageType typeEnum : values()) {
            if (typeEnum.oldType.equals(type)) {
                return typeEnum;
            }
        }

        return PLAIN_TEXT;
    }
}
