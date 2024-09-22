package net.letsdank.platform.module.base.esignDss.model.user;

import lombok.Getter;
import net.letsdank.platform.entity.common.User;
import net.letsdank.platform.utils.data.Either;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Getter
public class CryptoDSSPassword {
    private final String value;
    private final int state;
    private final int sign;

    // Alias: ОбъектПароля
    public CryptoDSSPassword(String value) {
        this(value, 0);
    }

    // Alias: ОбъектПароля
    public CryptoDSSPassword(String value, int state) {
        this(value, state, 1);
    }

    // Alias: ОбъектПароля
    public CryptoDSSPassword(String value, int state, int sign) {
        this(value, state, sign, null);
    }

    // Alias: ОбъектПароля
    public CryptoDSSPassword(String value, int state, int sign, Object settingsSource) {
        this.state = state;
        this.sign = sign;

        if (state == 5) {
            this.value = UUID.randomUUID().toString();
            // TODO: putSessionParameterValue(this.value, value, settingsSource);
        } else {
            this.value = value;
        }
    }

    /**
     * Returns a new CryptoDSSPassword object.
     * @param value String or CryptoDSSPassword of open password value.
     * @param authorizedUser User or String of user ID.
     * @return new CryptoDSSPassword object.
     */
    // Alias: ПодготовитьОбъектПароля
    public static CryptoDSSPassword preparePassword(Either<String, CryptoDSSPassword> value, Either<User, String> authorizedUser) {
        return preparePassword(value, authorizedUser, 1);
    }

    /**
     * Returns a new CryptoDSSPassword object.
     * @param value String or CryptoDSSPassword of open password value.
     * @param authorizedUser User or String of user ID.
     * @param sign Closing algorithm.
     * @return new CryptoDSSPassword object.
     */
    // Alias: ПодготовитьОбъектПароля
    public static CryptoDSSPassword preparePassword(Either<String, CryptoDSSPassword> value, Either<User, String> authorizedUser, int sign) {
        return preparePassword(value, authorizedUser, sign, null);
    }

    /**
     * Returns a new CryptoDSSPassword object.
     * @param value String or CryptoDSSPassword of open password value.
     * @param authorizedUser User or String of user ID.
     * @param sign Closing algorithm.
     * @param settingsSource Settings source.
     * @return new CryptoDSSPassword object.
     */
    // Alias: ПодготовитьОбъектПароля
    public static CryptoDSSPassword preparePassword(Either<String, CryptoDSSPassword> value, Either<User, String> authorizedUser, int sign, String settingsSource) {
        if (value.isRight()) {
            CryptoDSSPassword password = value.right();
            if (password.state == 0 && password.value != null && !password.value.isEmpty()) {
                return new CryptoDSSPassword(closeUserSecret(password.value, authorizedUser, sign), 1, 1, settingsSource);
            }

            return password;
        }

        if (value.left() == null)
            return new CryptoDSSPassword(null, 3, 1, settingsSource);
        if (value.left().isEmpty())
            return new CryptoDSSPassword(value.left(), 4, 1, settingsSource);
        if (sign != 1)
            return new CryptoDSSPassword(closeUserSecret(value.left(), authorizedUser, sign), 1, 1, settingsSource);

        return new CryptoDSSPassword(value.left(), 0, 1, settingsSource);
    }

    /**
     * Eject text representation of password from object.
     * @param authorizedUser User or String of user ID.
     * @return Password value.
     */
    // Alias: ПолучитьЗначениеПароля
    public String getPasswordValue(Either<User, String> authorizedUser) {
        if (authorizedUser.isLeft()) {
            return switch (state) {
                case 0 -> closeUserSecret(value, authorizedUser);
                case 1 -> value;
                default -> "";
            };
        }

        return this.toString(); // TODO: Правильно?
    }

    /**
     * Obfuscates password value from provided user input, before it will be sent to server.
     * @param inputSecret String of user secret.
     * @param authorizedUser Authorized user ID.
     * @return Obfuscated string.
     */
    // TODO: В ООП утилиту
    // Alias: ЗакрытьСекретПользователя
    private static String closeUserSecret(String inputSecret, Either<User, String> authorizedUser) {
        return closeUserSecret(inputSecret, authorizedUser, 1);
    }

    /**
     * Obfuscates password value from provided user input, before it will be sent to server.
     * @param inputSecret String of user secret.
     * @param authorizedUser Authorized user ID.
     * @param privacySign applying algorithm.
     * @return Obfuscated string.
     */
    // TODO: В ООП утилиту
    // Alias: ЗакрытьСекретПользователя
    private static String closeUserSecret(String inputSecret, Either<User, String> authorizedUser, int privacySign) {
        if (privacySign == 0) {
            return inputSecret;
        }

        String userData = authorizedUser.isRight() ? authorizedUser.right() : authorizedUser.left().getId().toString();

        byte[] key = (userData.substring(0, 6) + "_" + inputSecret).getBytes(StandardCharsets.UTF_8);
        int mod = key.length;
        // TODO: Здесь мы должны передавать строку в 16-ричном формате
        byte[] randomness = userData.replace("-", "").getBytes(StandardCharsets.UTF_8);

        byte[] shuffled = new byte[mod];
        for (int i = 0; i < mod; i++) {
            shuffled[i] = (byte) (i + 1);
        }

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < mod; i++) {
            int index = (i + 1) % randomness.length;
            int value = randomness[index] % mod;
            int foundValue = findIndex(shuffled, value);
            int foundIndex = findIndex(shuffled, i);

            if (foundValue != -1 && foundIndex != -1) {
                shuffled[foundIndex] = (byte) value;
                shuffled[foundValue] = (byte) (i + 1);
            }
        }

        byte[] result = new byte[key.length];
        int start = randomness[8] % mod;

        byte key1 = randomness[6];
        byte key2 = randomness[7];

        for (int i = 0; i < mod; i++) {
            int nextIndex = shuffled[start] - 1;
            byte nextByte = key[nextIndex];
            result[i] = nextByte;
            start = (start + 1) % mod;
        }

        for (int i = 0; i < mod; i++) {
            byte nextByte = result[i];
            nextByte = (byte) ((i % 2 == 1) ? nextByte + key1 : nextByte + key2);
            result[i] = nextByte;
        }

        return Base64.getEncoder().encodeToString(result);
    }

    private static int findIndex(byte[] array, int value) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == value) return i;
        return -1;
    }
}
