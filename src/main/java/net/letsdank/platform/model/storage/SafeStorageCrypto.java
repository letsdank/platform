package net.letsdank.platform.model.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SafeStorageCrypto {
    @Value("${platform.safe-storage.secret}")
    private String secret;

    public String encrypt(String plainText) {
        return plainText; // TODO: encrypt
    }

    public String decrypt(String encryptedText) {
        return encryptedText; // TODO: decrypt
    }
}
