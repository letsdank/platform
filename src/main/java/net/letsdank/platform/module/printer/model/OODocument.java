package net.letsdank.platform.module.printer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Структура документа
 */
@Data
public class OODocument {
    /**
     * Коллекция шаблонов областей, в которой ключ -
     * имя области в исходном шаблоне.
     */
    private Map<String, OODocumentArea> documentAreas;

    /**
     * Коллекция разделов шаблона, в которой ключ -
     * номер раздела в исходном шаблоне.
     */
    private Map<Integer, OOSection> sections;

    /**
     * Коллекция колонтитулов шаблона, в которой ключ -
     * имя верхнего или нижнего колонтитула, сформированное
     * из исходного шаблона.
     */
    private Map<String, OOHeaderFooter> headersFooters;

    /**
     * Коллекция заполненных и выведенных областей в конечном
     * документе.
     */
    private List<OOAttachedArea> attachedAreas;

    /**
     * Текст файла [Content_Types].xml из контейнера DOCX.
     */
    private String contentTypes;

    /**
     * Текст файла document.xml.rels из контейнера DOCX.
     */
    private String contentRelationships;

    /**
     * Разобранный файл document.xml.rels по именам и
     * идентификаторам ресурсов.
     */
    private Map<String, Object> contentRelationshipsTable;

    /**
     * Путь для сохранения картинок в конечном документе.
     */
    private String imageCatalog;

    /**
     * Расширения добавленных изображений в конечный документ.
     */
    private String[] imageExtensions;

    /**
     * Идентификатор ревизии документа.
     */
    private String id;

    public OODocument() {
        this.documentAreas = new HashMap<>();
        this.sections = new HashMap<>();
        this.headersFooters = new HashMap<>();
        this.attachedAreas = new ArrayList<>();
        this.contentTypes = "";
        this.contentRelationships = "";
        this.contentRelationshipsTable = new HashMap<>();
        this.imageCatalog = "";
        this.imageExtensions = new String[0];
        this.id = "";
    }
}
