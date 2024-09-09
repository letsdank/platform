package net.letsdank.platform.utils.mail;

import net.letsdank.platform.module.email.model.EmailMessageAddress;
import net.letsdank.platform.utils.platform.PUtils;
import net.letsdank.platform.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MailUtils {

    /**
     * Parses the text of an email addresses. While parsing the addresses, checks for validity.
     *
     * @param addresses The email addresses, delimiter - ',' or ';':<br>
     *             Sender1 &lt;Address1&gt;, Sender2 &lt;Address2&gt; ... SenderN &lt;AddressN&gt;
     * @return A list of parsed addresses.
     *
     * @see MailAddressParserResult
     */
    // Alias: АдресаЭлектроннойПочтыИзСтроки
    public static List<MailAddressParserResult> getAddressesFromText(String addresses) {
        List<MailAddressParserResult> result = new ArrayList<>();
        String brackets = "()[]";
        addresses = String.join(" ", addresses.split(Pattern.quote(brackets + " ")));
        addresses = addresses.replaceAll(">", ">;");

        for (String row : addresses.split(";")) {
            List<String> viewParts = new ArrayList<>();
            String name, address, error;

            for (String part : row.split(",")) {
                if (part == null || part.isEmpty()) {
                    viewParts.add(part);
                    continue;
                }

                List<String> stringParts = new ArrayList<>(Arrays.stream(PUtils.trimRight(part).split(" ")).toList());
                address = stringParts.get(stringParts.size() - 1);
                name = "";
                error = "";

                if (address.contains("@") || address.contains("<") || address.contains(">")) {
                    address = String.join("", address.split(Pattern.quote("<>")));
                    address = address.replaceAll("<", "").replaceAll(">", "");

                    if (emailAddressIsValid(address)) {
                        stringParts.remove(stringParts.size() - 1);
                    } else {
                        error = StringUtils.substituteParameters("Incorrect email address: \"%1\"", address);
                        address = "";
                    }

                    name = (String.join(",", viewParts) + String.join(" ", stringParts)).trim();
                    viewParts.clear();
                    result.add(new MailAddressParserResult(name, address, error));
                } else {
                    viewParts.add(part);
                }
            }

            name = String.join(",", viewParts);
            if (!name.isEmpty()) {
                address = "";
                error = StringUtils.substituteParameters("Incorrect email address: \"%1\"", name);
                result.add(new MailAddressParserResult(name, address, error));
            }
        }

        return result;
    }

    // Alias: РазобратьСтрокуСПочтовымиАдресами
    public static List<EmailMessageAddress> parseAddresses(String text) {
        return parseAddresses(text, true);
    }

    // Alias: РазобратьСтрокуСПочтовымиАдресами
    public static List<EmailMessageAddress> parseAddresses(String text, boolean throwException) {
        List<EmailMessageAddress> result = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<MailAddressParserResult> addresses = getAddressesFromText(text);

        for (MailAddressParserResult address : addresses) {
            if (!address.error().isEmpty()) {
                errors.add(address.error());
            }

            result.add(new EmailMessageAddress(address.address(), address.name()));
        }

        if (throwException && !errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        return result;
    }

    // Alias: АдресЭлектроннойПочтыСоответствуетТребованиям
    public static boolean emailAddressIsValid(String address) {
        return emailAddressIsValid(address, false);
    }

    // Alias: АдресЭлектроннойПочтыСоответствуетТребованиям
    public static boolean emailAddressIsValid(String address, boolean allowLocalAddresses) {
        String letters = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        String digits = "0123456789";
        String specialChars = ".@_-:+";

        // Check for exactly one @ symbol
        if (address.split("@").length != 2) {
            return false;
        }

        // Check for no more than one ":" symbol
        if (address.split(":").length > 2) {
            return false;
        }

        // Check for no consecutive dots
        if (address.contains("..")) {
            return false;
        }

        // Convert email address to lowercase
        address = address.toLowerCase();

        // Check for only allowed characters
        if (!containsOnlyAllowedChars(address, letters + digits + specialChars)) {
            return false;
        }

        // Split email into local name and domain
        int atIndex = address.indexOf("@");
        String localName = address.substring(0, atIndex);
        String domain = address.substring(atIndex + 1);

        // Check for non-empty local part and domain, and length limits
        if (localName.isEmpty() || domain.isEmpty() || localName.length() > 64 || domain.length() > 255) {
            return false;
        }

        // Check for no special characters at start or end of domain
        if (hasSpecialCharsAtStartOrEnd(domain, specialChars)) {
            return false;
        }

        // Check for at least one dot in domain, unless allowing local addresses
        if (!allowLocalAddresses && !domain.contains(".")) {
            return false;
        }

        // Check for no underscore, colon or plus in domain
        if (domain.contains("_") || domain.contains(":") || domain.contains("+")) {
            return false;
        }

        // Extract top-level domain (TLD) from domain
        String tld = domain;
        int dotIndex = domain.indexOf(".");
        while (dotIndex > 0) {
            tld = domain.substring(dotIndex + 1);
            dotIndex = tld.indexOf(".");
        }

        // Check TLD length and characters
        return allowLocalAddresses || (tld.length() >= 2 && containsOnlyAllowedChars(tld, letters));
    }

    // Alias: ЕстьСимволыВНачалеВКонце
    private static boolean hasSpecialCharsAtStartOrEnd(String str, String specialChars) {
        for (int i = 0; i < specialChars.length(); i++) {
            char symbol = specialChars.charAt(i);
            if (str.startsWith(String.valueOf(symbol)) || str.endsWith(String.valueOf(symbol))) {
                return true;
            }
        }
        return false;
    }

    // Alias: СтрокаСодержитТолькоДопустимыеСимволы
    private static boolean containsOnlyAllowedChars(String str, String allowedChars) {
        for (int i = 0; i < str.length(); i++) {
            char symbol = str.charAt(i);
            if (!allowedChars.contains(String.valueOf(symbol))) {
                return false;
            }
        }
        return true;
    }
}
