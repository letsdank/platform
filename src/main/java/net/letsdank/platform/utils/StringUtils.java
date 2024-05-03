package net.letsdank.platform.utils;

// Alias:
// СтроковыеФункцииКлиентСервер
public class StringUtils {
    /**
     * Substitutes parameters into a string. This function has no limits of number
     * of parameters.
     * Parameters in the string are specified as %&lt;parameter number&gt;. Parameter
     * numbering starts from one.
     *
     * @param templateString template string with parameters (entries in the form of
     *                       "%&lt;parameter number&gt;", for example, <code>"%1 went to %2"</code>).
     * @param parameters values of the parameters to substitute
     * @return text string with substituted parameters.
     * <p>
     * Example:
     * <pre>
     * StringUtils.substituteParameters("%1 went to %2", "Vasya", "Zoo") = "Vasya went to Zoo".
     * </pre>
     */
    // Alias: ПодставитьПараметрыВСтроку
    public static String substituteParameters(String templateString, String... parameters) {
        boolean hasParametersWithPercent = false;
        for (String parameter : parameters) {
            if (parameter.contains("%")) {
                hasParametersWithPercent = true;
                break;
            }
        }

        if (hasParametersWithPercent) {
            return substituteParametersWithPercent(templateString, parameters);
        }

        for (int i = 1; i < parameters.length + 1; i++) {
            templateString = templateString.replace("%"+(i), parameters[i-1]);
        }

        return templateString;
    }

    // Substitutes parameters into a string with percent
    // Alias: ПодставитьПараметрыСПроцентом
    private static String substituteParametersWithPercent(String templateString, String... parameters) {
        StringBuilder result = new StringBuilder();
        int position = templateString.indexOf("%");

        while (position > -1) {
            result.append(templateString, 0, position);
            char symbolAfterPercent = templateString.charAt(position + 1);

            String parameter = null;
            if (symbolAfterPercent >= '1' && symbolAfterPercent <= '9') {
                parameter = parameters[(int) symbolAfterPercent - '0'];
            }

            if (parameter == null) {
                result.append("%");
                templateString = templateString.substring(position + 1);
            } else {
                result.append(parameter);
                templateString = templateString.substring(position + 2);
            }
            position = templateString.indexOf("%");
        }
        result.append(templateString);
        return result.toString();
    }

    /**
     * Checks if a string contains only digits.
     *
     * @param value the string to be checked
     * @return <code>true</code> if the string contains only digits or is empty,
     * <code>false</code> if the string contains other characters
     * @see #isDigitsOnly(String, boolean)
     */
    public static boolean isDigitsOnly(String value) {
        return isDigitsOnly(value, true);
    }

    /**
     * Checks if a string contains only digits.
     *
     * @param value the string to be checked
     * @param spacesDisallowed if <code>false</code>, spaces are not allowed in the string
     * @return <code>true</code> if the string contains only digits or is empty,
     * <code>false</code> if the string contains other characters
     * <p>Example:</p>
     * <pre>
     * isDigitsOnly("0123"); // true
     * isDigitsOnly("0123abc"); // false
     * isDigitsOnly("01 2 3", false); // true
     * </pre>
     */
    public static boolean isDigitsOnly(String value, boolean spacesDisallowed) {
        if (!spacesDisallowed) {
            value = value.replace(" ", "");
        }

        if (value.isEmpty()) return true;

        return value.matches("\\d*");
    }
}
