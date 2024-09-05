package net.letsdank.platform.utils.fs;

import net.letsdank.platform.utils.string.StringUtils;

import java.io.File;
import java.io.IOException;

// Alias: ФайловаяСистема
public class FilesystemTempFiles {

    // Alias: СоздатьВременныйКаталог
    // TODO: JavaDoc
    public static String createTempDir() throws IOException {
        return createTempDir("");
    }

    // Alias: СоздатьВременныйКаталог
    // TODO: JavaDoc
    public static String createTempDir(String extension) throws IOException {
        File tempFile = File.createTempFile("platform", extension);
        boolean result = tempFile.mkdirs();
        if (!result) {
            throw new IllegalArgumentException("Failed to create temp directory");
        }

        return FilesystemUtils.appendSeparator(tempFile.getAbsolutePath());
    }

    // Alias: УдалитьВременныйКаталог
    // TODO: JavaDoc
    public static boolean deleteTempDir(String path) throws IOException  {
        if (!isTempDirectory(path)) {
            throw new IllegalArgumentException(StringUtils.substituteParameters(
                    "Invalid argument value %1 in %2: File %3 is not a temporary directory.",
                    "path", "FilesystemTempFiles.deleteTempDir", path
            ));
        }

        return new File(path).delete();
    }

    // Alias: УникальноеИмяФайла
    // TODO: JavaDoc
    public static String getUniqueFileName(String fileName) {
        File file = new File(fileName);
        String nameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf("."));
        String extension  = file.getName().substring(file.getName().lastIndexOf("."));
        String directory = file.getParent();

        int counter = 1;
        while (file.exists()) {
            counter++;
            file = new File(directory, nameWithoutExtension + " (" + counter + ")" + extension);
        }

        return file.getAbsolutePath();
    }

    // Alias: ЭтоИмяВременногоФайла
    private static boolean isTempDirectory(String path) throws IOException   {
        // Ожидается, что Путь получен методом File.createTempDir().
        return path.matches("^.*\\/temp\\-dir\\-[0-9]+\\/.*$");
    }
}
