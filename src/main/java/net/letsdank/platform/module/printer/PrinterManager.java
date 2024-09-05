package net.letsdank.platform.module.printer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Alias: УправлениеПечатьюСлужебный
public class PrinterManager {

    public static void copyPathRecursive(String src, String dst) throws IOException {
        File dstFile = new File(dst);

        if (dstFile.exists()) {
            if (dstFile.isFile()) {
                dstFile.delete();
                dstFile.mkdir();
            }
        } else {
            dstFile.mkdirs();
        }

        File srcFile = new File(src);
        for (File f : srcFile.listFiles())  {
            if (f.isDirectory()) {
                copyPathRecursive(f.getAbsolutePath(),
                        setPathSeparator(dst + "\\" + f.getName()));
            } else {
                Files.copy(f.toPath(), Path.of(setPathSeparator(dst + "\\" + f.getName())));
            }
        }
    }

    public static String setPathSeparator(String path) {
        return String.join(File.separator, path.split("\\/"));
    }
}
