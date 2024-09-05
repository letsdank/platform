package net.letsdank.platform.utils.fs;

import java.io.File;

public class FilesystemUtils {

    // Alias: ДобавитьКонечныйРазделительПути
    // TODO: JavaDoc
    public static String appendSeparator(String path) {
        return appendSeparator(path, null);
    }

    // Alias: ДобавитьКонечныйРазделительПути
    // TODO: JavaDoc
    public static String appendSeparator(String path, String platform) {
        if (path == null || path.isEmpty()) {
            return path;
        }

        return path.endsWith(File.separator) ? path : path + File.separator;
    }
}
