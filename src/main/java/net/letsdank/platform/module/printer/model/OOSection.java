package net.letsdank.platform.module.printer.model;

import lombok.Data;

import java.util.Map;

/**
 * Структура раздела
 */
@Data
public class OOSection {
    /**
     * Коллекция верхних и нижних колонтитулов шаблона
     * для конкретного раздела, в которой ключ -
     * имя колонтитула в исходном шаблоне.
     */
    private Map<String, OOHeaderFooter> headers;

    /**
     * Текст раздела в исходном шаблоне.
     */
    private String text;

    /**
     * Номер раздела в исходном шаблоне.
     */
    private int number;
}
