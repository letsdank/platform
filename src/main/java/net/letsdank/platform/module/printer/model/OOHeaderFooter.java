package net.letsdank.platform.module.printer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Структура верхнего/нижнего колонтитула
 */
@Data
public class OOHeaderFooter {
    /**
     * Имя колонтитула, сформированное из исходного шаблона.
     */
    private String name;

    /**
     * Имя файла колонтитула из структуры контейнера DOCX
     * исходного шаблона.
     */
    private String nameInternal;

    /**
     * Текст колонтитула, заданный в исходном шаблоне.
     */
    private String text;

    /**
     * Номер раздела в исходном шаблоне, к которому относится
     * колонтитул.
     */
    private int sectionNumber;

    private List<String> hyperlinks;

    public OOHeaderFooter() {
        this.name = "";
        this.nameInternal = "";
        this.text = "";
        this.sectionNumber = 1;
        this.hyperlinks = new ArrayList<>();
    }
}
