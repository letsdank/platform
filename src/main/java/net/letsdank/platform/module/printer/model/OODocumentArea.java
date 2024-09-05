package net.letsdank.platform.module.printer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Структура области
 */
@Data
public class OODocumentArea {
    /**
     * Имя области, заданное в исходном шаблоне.
     */
    private String name;

    /**
     * Текст области, заданный в исходном шаблоне.
     */
    private String text;

    /**
     * Номер раздела в исходном шаблоне, в который входит область.
     */
    private int sectionNumber;

    private List<String> hyperlinks;

    public OODocumentArea() {
        this.name = "";
        this.text = "";
        this.sectionNumber = 1;
        this.hyperlinks = new ArrayList<>();
    }
}
