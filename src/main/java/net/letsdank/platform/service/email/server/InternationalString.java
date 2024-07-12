package net.letsdank.platform.service.email.server;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// Класс со строковым значением на разных языках
public class InternationalString {
    private final Map<String, String> map = new HashMap<>();

    public void addValue(String key, String value) {
        map.put(key, value);
    }

    public String getValueFor(String lang) {
        return map.get(lang);
    }

    public String getValue() {
        return map.get(getCurrentLocale().getLanguage());
    }

    private static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}
