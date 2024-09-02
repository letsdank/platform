package net.letsdank.platform.utils.platform.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO: Продумать
public class InternetMailMessageAttachment {
    private String name;
    private String id;
    private String contentType;
    private String encoding;
    private byte[] data;

    public InternetMailMessageAttachment(byte[] data, String id) {
        this.data = data;
        this.id = id;
    }
}
