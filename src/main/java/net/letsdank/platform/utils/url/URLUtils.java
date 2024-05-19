package net.letsdank.platform.utils.url;

import net.letsdank.platform.utils.string.StringUtils;

// Класс-утилита для работы с URL адресами
public class URLUtils {

    public static URLInfo getURLInfo(String url) {
        url = url.trim();

        // Схема
        String scheme = "";
        int position = url.indexOf("://");
        if (position > 0) {
            scheme = url.substring(0, position).toLowerCase();
            url = url.substring(position + 3);
        }

        // Строка соединения и путь на сервере.
        String connection = url;
        String path = "";
        position = connection.indexOf("/");
        if (position > 0) {
            path = connection.substring(position);
            connection = connection.substring(0, position);
        }

        // Информация пользователя и имя сервера.
        String auth = "";
        String serverName = connection;
        position = connection.lastIndexOf("@");
        if (position > 0) {
            auth = serverName.substring(0, position);
            serverName = serverName.substring(position + 1);
        }

        // Логин и пароль
        String login = auth;
        String password = "";
        position = auth.indexOf(":");
        if (position > 0) {
            login = auth.substring(0, position - 1);
            password = auth.substring(position + 1);
        }

        // Хост и порт
        String host = serverName;
        String port = "";
        position = serverName.indexOf(":");
        if (position > 0) {
            host = serverName.substring(0, position);
            port = serverName.substring(position + 1);
            if (!StringUtils.isDigitsOnly(port)) {
                port = "";
            }
        }

        return new URLInfo(scheme,
                login,
                password,
                serverName,
                host,
                port.isEmpty() ? null : Integer.parseInt(port),
                path);
    }
}
