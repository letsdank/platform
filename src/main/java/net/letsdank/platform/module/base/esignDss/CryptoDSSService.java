package net.letsdank.platform.module.base.esignDss;

import net.letsdank.platform.entity.esignDss.DSSAccount;
import net.letsdank.platform.module.base.esignDss.model.CryptoDSSServiceResponse;

// Alias: СервисКриптографииDSS
public class CryptoDSSService {
    // Alias: Зашифровать
    public static CryptoDSSServiceResponse encrypt(DSSAccount user, String data, Object receiverCertificates) {
        return encrypt(user, data, receiverCertificates, "CMS");
    }

    // Alias: Зашифровать
    public static CryptoDSSServiceResponse encrypt(DSSAccount user, String data, Object receiverCertificates, String encryptionType) {
        return encrypt(user, data, receiverCertificates, encryptionType, null);
    }

    // Alias: Зашифровать
    public static CryptoDSSServiceResponse encrypt(DSSAccount user, String data, Object receiverCertificates, String encryptionType,
                                                   Object operationParams) {
        // Object userParams = getUserParams(user, operationParams);
        // TODO: Implement
        return null;
    }

    // TODO: Возможно, перенести в ООП как статику

}
