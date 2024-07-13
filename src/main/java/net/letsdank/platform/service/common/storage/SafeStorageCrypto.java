package net.letsdank.platform.service.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Сервис шифрования SafeStorage с использованием AES.
 */
@Service
public class SafeStorageCrypto {
    @Value("${platform.safe-storage.secret}")
    private String secret;

    public String encrypt(String plainText) {
        try {
            // Зашифровываем plainText с использованием secret через AES
            return encrypt(plainText, generateSecretKey());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e); // TODO: Дописать сообщение об ошибке
        }
    }

    public String decrypt(String encryptedText) {
        try {
            // Расшифровываем encryptedText с использованием secret через AES
            return decrypt(encryptedText, generateSecretKey());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e); // TODO: Дописать сообщение об ошибке
        }
    }

    private SecretKey generateSecretKey() {
        byte[] key = secret.getBytes();
        return new SecretKeySpec(key, "AES");
    }

    private String encrypt(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decrypt(String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }
}
