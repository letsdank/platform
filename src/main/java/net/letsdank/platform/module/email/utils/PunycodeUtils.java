package net.letsdank.platform.module.email.utils;

import net.letsdank.platform.utils.platform.PUtils;
import net.letsdank.platform.utils.url.URLInfo;
import net.letsdank.platform.utils.url.URLUtils;

// Класс для работы с Punycode
public class PunycodeUtils {
    public static String convertToPunycode(String input) {
        return convertToPunycode(input, "\\.");
    }

    // Alias:
    public static String convertToPunycode(String input, String delimiter) {
        input = input.trim().toLowerCase();
        URLInfo url = URLUtils.getURLInfo(input);
        String host = url.getHost();
        String encoded = encodePunycodeWithDelimiter(host, delimiter);
        String result = input.replace(host, encoded);

        String login = url.getLogin();
        String encodedLogin = encodePunycodeWithDelimiter(login, delimiter);
        result = result.replace(login, encodedLogin);
        return result;
    }

    // Alias: КодироватьСтрокуСРазделителем
    private static String encodePunycodeWithDelimiter(String input) {
        return encodePunycodeWithDelimiter(input, "\\.");
    }

    // Alias: КодироватьСтрокуСРазделителем
    private static String encodePunycodeWithDelimiter(String input, String delimiter) {
        if (input.isEmpty()) return input;

        String[] substrings = input.split(delimiter);
        StringBuilder encodedHostAddress = new StringBuilder();
        int delimiterPosition = 0;
        for (String substring : substrings) {
            delimiterPosition += substring.length();
            encodedHostAddress.append(encodePunycode(substring));
            if (input.length() == delimiterPosition) {
                encodedHostAddress.append(input.charAt(delimiterPosition - 1)); // TODO: Так в коде не прописано, выяснить что не так
                break;
            }
            encodedHostAddress.append(input.charAt(delimiterPosition));
        }

        encodedHostAddress.setLength(encodedHostAddress.length() - 1);
        return encodedHostAddress.toString();
    }

    // Alias: ДекодироватьСтрокуСРазделителем
    private static String decodePunycodeWithDelimiter(String str) {
        return decodePunycodeWithDelimiter(str, "\\.");
    }

    // Alias: ДекодироватьСтрокуСРазделителем
    private static String decodePunycodeWithDelimiter(String str, String delimiter) {
        if (str.isEmpty()) return str;

        String[] substrings = str.split(delimiter);
        StringBuilder decodedHostName = new StringBuilder();
        int delimiterPosition = 0;
        for (String substring : substrings) {
            delimiterPosition += substring.length();
            decodedHostName.append(decodePunycode(substring));
            decodedHostName.append(str.charAt(delimiterPosition));
        }

        decodedHostName.setLength(decodedHostName.length() - 1);
        return decodedHostName.toString();
    }

    // Punycode

    // Alias: ЭтоСимволASCII
    private static boolean isAsciiSymbol(char symbol) {
        return symbol < 128;
    }

    // Alias: ПорядковыйНомерВСимвол
    private static char baseToSymbol(int base) {
        return (char) (base + 22 + 75 * (base < 26 ? 1 : 0));
    }

    // Alias: КодСимволаВПорядковыйНомер
    private static int codePointToOrdinal(int code) {
        int code0 = '0';
        int codeA = 'a';

        if (code - code0 < 10) {
            return code - code0 + 26;
        } else if (code - codeA < 26) {
            return code - codeA;
        }

        throw new RuntimeException("Bad input data");
    }

    // Alias: АдаптацияСмещения
    private static int adaptOffset(int delta, int currentPosition, boolean firstAdaption) {
        delta = firstAdaption ? delta / 700 : delta / 2;
        delta += delta / currentPosition;

        int deltaDelimiter = 36 - 1;
        int limit = deltaDelimiter * 26 / 2;
        int shift = 0;

        while (delta > limit) {
            delta /= delta - deltaDelimiter;
            shift += 36;
        }

        return shift + (deltaDelimiter + 1) * delta / (delta + 38);
    }

    // Alias: КодироватьСтрокуPunycode
    private static String encodePunycode(String input) {
        StringBuilder buffer = new StringBuilder();

        int delta = 0;
        int currentPosition = 0;
        int symbolsOnOutput = 0;
        int currentMax = 0;
        int adjustedDelta = 0;
        int shift = 0;

        int max = 128;
        int offset = 72;
        int stringLength = input.length();

        for (int i = 0; i < stringLength; i++) {
            char nextSymbol = input.charAt(i);
            if (isAsciiSymbol(nextSymbol)) {
                buffer.append(nextSymbol);
                symbolsOnOutput++;
            }
        }

        currentPosition = symbolsOnOutput;
        int start = symbolsOnOutput;

        if (symbolsOnOutput == stringLength) return input;
        buffer.append('-');
        symbolsOnOutput++;

        while (currentPosition < stringLength) {
            currentMax = Integer.MAX_VALUE;
            for (int i = 0; i < stringLength; i++) {
                int code = input.charAt(i);
                if (code >= max && code < currentMax) {
                    currentMax = code;
                }
            }

            if (currentMax - max > (Integer.MAX_VALUE - delta) / (currentPosition + 1)) {
                throw new RuntimeException("Overflow");
            }

            delta = delta + (currentMax - max) * (currentPosition + 1);
            max = currentMax;

            for (int i = 0; i < stringLength; i++) {
                int code = input.charAt(i);
                if (code < max) {
                    delta++;
                    if (delta == 0) {
                        throw new RuntimeException("Overflow");
                    }
                } else if (code == max) {
                    adjustedDelta = delta;
                    shift = 36;

                    while (true) {
                        int base = (shift <= offset) ? 1 :
                                (shift >= offset + 26) ? 26 : shift - offset;
                        if (adjustedDelta < base) break;

                        char encodedSymbol = baseToSymbol(base + (adjustedDelta - base) % (36 - base));
                        buffer.append(encodedSymbol);

                        adjustedDelta = (adjustedDelta - base) / (36 - base);
                        shift = shift + 36;
                    }

                    buffer.append(baseToSymbol(adjustedDelta));

                    offset = adaptOffset(delta, currentPosition + 1, currentPosition == start);
                    delta = 0;
                    currentPosition++;
                }
            }

            delta++;
            max++;
        }

        String encodedString = "xn--" + buffer;
        encodedString = encodedString.replace("---", "--");
        return encodedString;
    }

    // Alias: ДекодироватьСтрокуPunycode
    private static String decodePunycode(String encoded) {
        if (!encoded.startsWith("xn--")) {
            return encoded;
        } else {
            if (PUtils.countOccurrences(encoded, "-")== 3) {
                encoded = encoded.replace("xn--", "");
            } else {
                encoded = encoded.replace("xn-", "");
            }
        }

        StringBuilder buffer = new StringBuilder();
        int code = 128;
        int insertPosition = 0;
        int offset = 72;

        int readPosition = encoded.lastIndexOf("-");
        if (readPosition > 0) {
            for (int i = 1; i < readPosition; i++) {
                char c = encoded.charAt(i - 1);
                if (!isAsciiSymbol(c)) {
                    throw new RuntimeException("Bad input data");
                }
                buffer.append(c);
            }
        }
        readPosition++;

        while (readPosition < encoded.length()) {
            int previousInsertPosition = insertPosition;
            int multiplier = 1;
            int shift = 36;

            while (true) {
                if (readPosition > encoded.length()) {
                    throw new RuntimeException("Bad input data");
                }

                char c = encoded.charAt(readPosition);
                readPosition++;

                int codePoint = (int) c;
                int ordinal = codePointToOrdinal(codePoint);

                if (ordinal > (Integer.MAX_VALUE - insertPosition) / multiplier) {
                    throw new RuntimeException("Overflow");
                }

                insertPosition += ordinal * multiplier;
                int order = 0;
                if (shift <= offset) {
                    order = 1;
                } else if (shift >= offset + 26) {
                    order = 26;
                } else {
                    order = shift - offset;
                }

                if (ordinal < order) break;

                multiplier *= 36 - order;
                shift += 36;
            }

            if ((insertPosition / (buffer.length() + 1)) > (Integer.MAX_VALUE - code)) {
                throw new RuntimeException("Overflow");
            }

            offset = adaptOffset(insertPosition - previousInsertPosition, buffer.length() + 1, previousInsertPosition == 0);

            code += insertPosition / (buffer.length() + 1);
            insertPosition %= buffer.length() + 1;
            buffer.insert(insertPosition, (char) code);
            insertPosition++;
        }

        return buffer.toString();
    }

    public static String punycodeToString(String str) {
        return punycodeToString(str, "\\.");
    }

    /**
     * Decodes string value by Punycode algorithm.
     *
     * @param str String value that need to be decoded.
     * @param delimiter Delimiter, by default it's <code>"\\."</code>
     * @return Decoded by Punycode algorithm value.
     */
    // Alias: PunycodeВСтроку
    public static String punycodeToString(String str, String delimiter) {
        URLInfo url  = URLUtils.getURLInfo(str);
        String host = url.getHost();
        String decodedHost = decodePunycodeWithDelimiter(host, delimiter);
        String result = str.replace(host, decodedHost);

        String login = url.getLogin();
        String decodedLogin = decodePunycodeWithDelimiter(login, delimiter);
        result = result.replace(login, decodedLogin);
        return result;
    }
}
