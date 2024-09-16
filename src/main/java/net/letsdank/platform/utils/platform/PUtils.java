package net.letsdank.platform.utils.platform;

/**
 * The class of platform utility functions.
 */
// Возможно, он когда-нибудь не нужен будет, поскольку основные функции
// можно расположить по другим утилитным классам
public class PUtils {
    // TODO: Вынести как сессию
    private static boolean privileged = true;
    /**
     * Returns count of <code>substr</code> occurrences in <code>source</code> string.
     * @param source The source string.
     * @param substr The substr string.
     * @return The count of occurrences in <code>source</code> string.
     */
    public static int countOccurrences(String source, String substr) {
        int count = 0;
        int pos = 0;
        while ((pos = source.indexOf(substr, pos)) != -1) {
            count++;
            pos++;
        }
        return count;
    }

    /**
     * Returns string that will be trimmed only in left side.
     * @param str The string that need to be left-trimmed.
     * @return The left-trimmed string.
     */
    public static String trimLeft(String str) {
        if (str == null) return null;

        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }

        return str.substring(i);
    }

    /**
     * Returns string that will be trimmed only in right side.
     * @param str The string that need to be right-trimmed.
     * @return The right-trimmed string.
     */
    public static String trimRight(String str) {
        if (str == null) return null;

        int i = str.length();
        while (i > 0 && Character.isWhitespace(str.charAt(i - 1))) {
            i--;
        }

        return str.substring(0, i);
    }

    /**
     * Returns <code>true</code> if current thread is privileged.
     * @return <code>true</code> if current thread is privileged, otherwise <code>false</code>.
     */
    public static boolean isPrivileged() {
        return PUtils.privileged;
    }

    public static void setPrivileged(boolean privileged) {
        PUtils.privileged = privileged;
    }
}
