package net.letsdank.platform.utils.mail;

/**
 * Represents the result of parsing an email address.
 * @param name The name of the address
 * @param address The address that valid by the standards. If text that similar to an email address
 *                was not found, this text will be written to the name field.
 * @param error Text representation of the error, or empty if no error was found.
 */
public record MailAddressParserResult(String name, String address, String error) {
}
