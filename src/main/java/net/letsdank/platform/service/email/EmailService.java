package net.letsdank.platform.service.email;

import net.letsdank.platform.utils.url.URLInfo;
import net.letsdank.platform.utils.url.URLUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Alias:
// РаботаСПочтовымиСообщениями
// РаботаСПочтовымиСообщениямиСлужебный
public class EmailService {

    // Alias: АдресаСерверовDNS
    public static List<String> getDnsServers() {
        // TODO: Implement interface, then use class that inherited from interface.
        return new ArrayList<>(Arrays.asList("8.8.8.8", "8.8.4.4")); // dns.google
    }

    // Alias: ЭтоСимволASCII
    private static boolean isAsciiSymbol(char symbol) {
        return symbol < 128;
    }

    private static char baseToSymbol(int base) {
        return (char) (base + 22 + 75 * (base < 26 ? 1 : 0));
    }

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

    public static String convertToPunycode(String input) {
        input = input.trim().toLowerCase();
        URLInfo url = URLUtils.getURLInfo(input);
        String host = url.getHost();
        String encoded = encodePunycodeWithDelimiter(host);
        String result = input.replace(host, encoded);

        String login = url.getLogin();
        String encodedLogin = encodePunycodeWithDelimiter(login);
        result = result.replace(login, encodedLogin);
        return result;
    }

    private static String encodePunycodeWithDelimiter(String input) {
        return encodePunycodeWithDelimiter(input, "\\.");
    }

    private static String encodePunycodeWithDelimiter(String input, String delimiter) {
        if (input.isEmpty()) return input;

        String[] substrings = input.split(delimiter);
        StringBuilder encodedHostAddress = new StringBuilder();
        int delimiterPosition = 0;
        for (String substring : substrings) {
            delimiterPosition += substring.length();
            encodedHostAddress.append(encodePunycode(substring));
            encodedHostAddress.append(input.charAt(delimiterPosition));
        }

        encodedHostAddress.setLength(encodedHostAddress.length() - 1);
        return encodedHostAddress.toString();
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
}
