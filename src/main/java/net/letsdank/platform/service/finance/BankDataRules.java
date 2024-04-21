package net.letsdank.platform.service.finance;

// Alias:
// БанковскиеПравилаКлиентСервер
public class BankDataRules {
    // ISO 9362
    // SWIFT BIC

    public static final int SWIFT_OFFICE_LENGTH = 8;
    public static final int SWIFT_BRANCH_LENGTH = 11;

    /**
     * Retrieves the country code from the SWIFT code according to
     * ISO 9362.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return The SWIFT country code.
     */
    // Alias: КодСтраныSWIFT
    public static String getCountryCodeFromSwift(String swiftBic) {
        return swiftBic.substring(4, 6);
    }

    /**
     * Checks if the string conforms to the SWIFT code format.
     *
     * @param value The string to be checked for SWIFT code
     *              format compliance.
     * @return <code>true</code> if the string conforms to the SWIFT
     * code format, otherwise <code>false</code>.
     */
    // Alias: СтрокаСоответствуетФорматуSWIFT
    public static boolean isSwiftFormatValid(String value) {
        return isSwiftLengthValid(value) && isSwiftCharactersValid(value);
    }

    /**
     * Checks the length of the SWIFT code.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return <code>true</code> if the length of the SWIFT code is valid,
     * otherwise <code>false</code>.
     */
    // Alias: ПроверитьДлинуSWIFT
    public static boolean isSwiftLengthValid(String swiftBic) {
        int codeLength = swiftBic.length();
        return codeLength == SWIFT_OFFICE_LENGTH || codeLength == SWIFT_BRANCH_LENGTH;
    }

    /**
     * Checks if the string contains only valid SWIFT characters.
     *
     * @param swiftBic The string representing the SWIFT BIC code.
     * @return <code>true</code> if all characters in the string are valid
     * SWIFT characters, otherwise <code>false</code>.
     */
    // Alias: ПроверитьРазрешенныеСимволыSWIFT
    public static boolean isSwiftCharactersValid(String swiftBic) {
        return swiftBic.chars()
            .allMatch(symbol -> {
                char upperCaseSymbol = Character.toUpperCase((char) symbol);
                return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(upperCaseSymbol) != -1;
            });
    }
}
