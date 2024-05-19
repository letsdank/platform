package net.letsdank.platform.utils.url;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Класс, содержащий информацию о URL
@Getter
@AllArgsConstructor
public class URLInfo {
    private String scheme;      // Схема из URI
    private String login;       // Логин из URI
    private String password;    // Пароль из URI
    private String name;        // Часть <host><:port> из URI
    private String host;        // Хост из URI
    private Integer port;       // Порт из URI
    private String path;        // Часть <путь>?<параметры>#<якорь> из URI
}
