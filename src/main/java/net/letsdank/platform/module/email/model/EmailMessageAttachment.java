package net.letsdank.platform.module.email.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMessageAttachment {
    private String tempPath; // Адрес во временном хранилище
    private String encoding;
    private String id;

    private String name;
    private String key;
    private String contentType;
    private byte[] data;
}
