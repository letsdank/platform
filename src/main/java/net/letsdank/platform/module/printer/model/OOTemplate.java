package net.letsdank.platform.module.printer.model;

import lombok.Data;
import net.letsdank.platform.module.printer.PrinterManager;
import net.letsdank.platform.utils.fs.FilesystemTempFiles;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Макет - структура, используемая для хранения областей, разделов и
 * колонтитулов из исходного шаблона.
 */
@Data
public class OOTemplate {
    /**
     * Путь, куда распаковывается контейнер DOCX шаблона для
     * дальнейшего анализа.
     */
    private String catalogName;

    /**
     * Коллекция, куда собирается информация по областям,
     * разделам и колонтитулам, выведенных в конечный документ.
     */
    private OODocument document;

    // TODO: Здесь должен быть аргумент template
    public OOTemplate() {
        // TODO: Добавить поддержку безопасного режима
        try {
            String tempPath = FilesystemTempFiles.createTempDir();
            // TODO: Макет.ИмяКаталога
            PrinterManager.copyPathRecursive("document", tempPath);

            catalogName = tempPath;
            document = new OODocument();

            initializeData(); // TODD: Должен передаваться template

        } catch (IOException ignored) {

        }
    }

    private void initializeData() throws IOException {
        document.setId(""); // TODO: Макет.СтруктураДокумента.ИдентификаторДокумента
        document.setImageCatalog(catalogName + PrinterManager.setPathSeparator("\\word\\media\\"));
        // TODO: Макет.СтруктураДокумента.ТаблицаСвязейКонтента.Скопировать()
        //document.setContentRelationshipsTable();

        File file = new File(catalogName + "[Content_Types].xml");
        if (file.exists()) {
            document.setContentRelationships(Files.readString(file.toPath(), StandardCharsets.UTF_8));
        }

        String structurePath = catalogName + "word" + File.separator;

        for (File f : new File(structurePath).listFiles(new XmlFileFilter())) {
            String nameWithoutExtension  = f.getName().substring(0, f.getName().lastIndexOf("."));

            if (nameWithoutExtension.equals("document"))  {
                Files.writeString(f.toPath(), "", StandardCharsets.UTF_8);
            }
        }
    }

    private static final class XmlFileFilter implements FileFilter  {
        @Override
        public boolean accept(File pathname) {
            return pathname.getPath().endsWith(".xml");
        }
    }
}
