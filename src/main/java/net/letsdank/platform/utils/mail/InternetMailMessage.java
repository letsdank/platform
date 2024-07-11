package net.letsdank.platform.utils.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
// TODO: Продумать
public class InternetMailMessage {
    private String subject;
    private Map<String, String> to;
    private Pair<String, String> from;

    private Map<String, String> body;

    public InternetMailMessage() {
        to = new HashMap<>();
        from = Pair.of("", "");
        body = new HashMap<>();
    }

    public void addTo(String address, String name) {
        this.to.put(address, name);
    }

    public void setFrom(String address, String name) {
        this.from = Pair.of(address, name);
    }

    public void addBody(String text, String type) {
        this.body.put(text, type);
    }
}
