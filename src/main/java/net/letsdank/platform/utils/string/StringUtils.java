package net.letsdank.platform.utils.string;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Alias:
// СтроковыеФункцииКлиентСервер
public class StringUtils {
    /**
     * Splits a string into multiple strings by a specified delimiter. The delimiter can have
     * any length. In cases, where the delimiter is a single character, and the
     * <code>trimLines</code> parameter is not used, it is recommended to use the
     * <code>String.split("")</code>.
     *
     * @param value The string to be split, containing delimiters.
     * @return a list of strings.
     *
     * @see #splitStringToSubstringArray(String, String, Boolean, boolean)
     */
    // Alias: РазложитьСтрокуВМассивПодстрок
    public static List<String> splitStringToSubstringArray(String value) {
        return splitStringToSubstringArray(value, ",");
    }

    /**
     * Splits a string into multiple strings by a specified delimiter. The delimiter can have
     * any length. In cases, where the delimiter is a single character, and the
     * <code>trimLines</code> parameter is not used, it is recommended to use the
     * <code>String.split("")</code>.
     *
     * @param value The string to be split, containing delimiters.
     * @param delimiter The delimiter string, minimum 1 character.
     * @return a list of strings.
     *
     * @see #splitStringToSubstringArray(String, String, Boolean, boolean)
     */
    // Alias: РазложитьСтрокуВМассивПодстрок
    public static List<String> splitStringToSubstringArray(String value, String delimiter) {
        return splitStringToSubstringArray(value, delimiter, null);
    }

    /**
     * Splits a string into multiple strings by a specified delimiter. The delimiter can have
     * any length. In cases, where the delimiter is a single character, and the
     * <code>trimLines</code> parameter is not used, it is recommended to use the
     * <code>String.split("")</code>.
     *
     * @param value The string to be split, containing delimiters.
     * @param delimiter The delimiter string, minimum 1 character.
     * @param skipEmptyLines If this parameter is not specified, the function works in compatibility
     *                       mode with its previous version:
     *                       <ul>
     *                       <li>for <code>" "</code> delimiter, empty strings are not included in the result,
     *                       for other delimiters, empty strings are included;</li>
     *                       <li>if the <code>value</code> string does not contain significant characters
     *                       or is empty, the result will be an array containing one empty string for
     *                       <code>" "</code> delimiter, and an empty array for other delimiters.</li>
     *                       </ul>
     * @return a list of strings.
     *
     * @see #splitStringToSubstringArray(String, String, Boolean, boolean)
     */
    // Alias: РазложитьСтрокуВМассивПодстрок
    public static List<String> splitStringToSubstringArray(String value, String delimiter, Boolean skipEmptyLines) {
        return splitStringToSubstringArray(value, delimiter, skipEmptyLines, false);
    }

    /**
     * Splits a string into multiple strings by a specified delimiter. The delimiter can have
     * any length. In cases, where the delimiter is a single character, and the
     * <code>trimLines</code> parameter is not used, it is recommended to use the
     * <code>String.split("")</code>.
     *
     * @param value The string to be split, containing delimiters.
     * @param delimiter The delimiter string, minimum 1 character.
     * @param skipEmptyLines If this parameter is not specified, the function works in compatibility
     *                       mode with its previous version:
     *                       <ul>
     *                       <li>for <code>" "</code> delimiter, empty strings are not included in the result,
     *                       for other delimiters, empty strings are included;</li>
     *                       <li>if the <code>value</code> string does not contain significant characters
     *                       or is empty, the result will be an array containing one empty string for
     *                       <code>" "</code> delimiter, and an empty array for other delimiters.</li>
     *                       </ul>
     * @param trimLines Flag indicating whether to trim non-printable symbols at the edges of each
     *                  found substring.
     * @return a list of strings.
     * <p>
     * Example:
     * <pre>
     *     StringUtils.splitStringToSubstringArray(",one,,two,", ",") = ["", "one", "", "two", ""];<br>
     *     StringUtils.splitStringToSubstringArray(",one,,two,", ",", true) = ["one", "two"];<br>
     *     StringUtils.splitStringToSubstringArray(" one   two  ", " ") = ["one", "two"];<br>
     *     StringUtils.splitStringToSubstringArray("") = [];<br>
     *     StringUtils.splitStringToSubstringArray("", ",", false) = [""];<br>
     *     StringUtils.splitStringToSubstringArray("", " ") = [""];
     * </pre>
     */
    // Alias: РазложитьСтрокуВМассивПодстрок
    public static List<String> splitStringToSubstringArray(String value, String delimiter, Boolean skipEmptyLines, boolean trimLines) {
        if (delimiter.length() == 1 && skipEmptyLines == null && trimLines) {
            String[] result = value.split(delimiter);
            return Stream.of(result).map(String::trim).collect(Collectors.toList());
        }

        List<String> result = new ArrayList<>();

        // For backward compatibility.
        if (skipEmptyLines == null) {
            skipEmptyLines = delimiter.equals(" ");
            if (value.isEmpty()) {
                if (delimiter.equals(" ")) {
                    result.add("");
                }
                return result;
            }
        }

        int position = value.indexOf(delimiter);
        while (position > 0) {
            String substring = value.substring(0, position - 1);
            if (!skipEmptyLines || !substring.isEmpty()) {
                result.add(trimLines ? substring.trim() : substring);
            }

            value = value.substring(position + delimiter.length());
            position = value.indexOf(delimiter);
        }

        if (!skipEmptyLines || !value.isEmpty()) {
            result.add(trimLines ? value.trim() : value);
        }

        return result;
    }
    
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
