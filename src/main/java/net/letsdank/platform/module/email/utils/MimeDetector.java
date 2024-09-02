package net.letsdank.platform.module.email.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeDetector {
    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("xhtml", "application/xhtml+xml");
        MIME_TYPES.put("zip", "application/zip");
        MIME_TYPES.put("gzip", "application/gzip");

        MIME_TYPES.put("aac", "audio/aac");
        MIME_TYPES.put("ogg", "audio/ogg");
        MIME_TYPES.put("wma", "audio/x-ms-wma");
        MIME_TYPES.put("wav", "audio/vnd.wave");

        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("svg", "image/svg");
        MIME_TYPES.put("tiff", "image/tiff");
        MIME_TYPES.put("ico", "image/vnd.microsoft.icon");

        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("xml", "text/xml");

        MIME_TYPES.put("mpeg", "video/mpeg");
        MIME_TYPES.put("mp4", "video/mp4");
        MIME_TYPES.put("mov", "video/quicktime");
        MIME_TYPES.put("wmv", "video/x-ms-wmv");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("3gpp", "video/3gpp");
        MIME_TYPES.put("3gp", "video/3gp");
        MIME_TYPES.put("3gpp2", "video/3gpp2");
        MIME_TYPES.put("3g2", "video/3gpp2");

        MIME_TYPES.put("odt", "application/vnd.oasis.opendocument.text");
        MIME_TYPES.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        MIME_TYPES.put("odp", "application/vnd.oasis.opendocument.presentation");
        MIME_TYPES.put("odg", "application/vnd.oasis.opendocument.graphics");

        MIME_TYPES.put("doc", "application/msword");
        MIME_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_TYPES.put("xls", "application/vnd.ms-excel");
        MIME_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        MIME_TYPES.put("rar", "application/x-rar-compressed");

        MIME_TYPES.put("p7m", "application/x-pkcs7-mime");
        MIME_TYPES.put("p7s", "application/x-pkcs7-signature");
    }

    public static String getMimeTypeByFilename(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return MIME_TYPES.get(extension);
    }

    // Alias: ТипыСодержимого
    private static Map<String, String> getMimeTypes() {
        return MIME_TYPES;
    }
}
