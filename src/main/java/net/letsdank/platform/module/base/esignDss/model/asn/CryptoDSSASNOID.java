package net.letsdank.platform.module.base.esignDss.model.asn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryptoDSSASNOID {
    private static final List<CryptoDSSASNAttribute> OID_ATTRIBUTES = new ArrayList<>();

    static {
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.3", "CN", 64, "commonName", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.4", "SN", 64, "lastName", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.42", "G", 64, "firstMiddleName", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.6", "C", 2, "country", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.8", "S", 128, "regionCode", "10")); // 77 г. Москва
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.7", "L", 128, "locality", "10")); // "Москва г, Зеленоград г, Крюково п"
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.9", "Street", 128, "street", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.5", "OGRNIP", 15, "ogrnip", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.10", "O", 64, "organization", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.11", "OU", 64, "organizationUnit", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.12", "T", 64, "jobTitle", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.1", "OGRN", 13, "ogrn", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.3", "SNILS", 14, "snils", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.3.131.1.1", "INN", 12, "inn", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.840.113549.1.9.1", "E", 128, "email", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.4", "INNLE", 10, "innle", "10"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.114", "identificationKind", 1, "identificationKind", "10"));

        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.5", "SerialNumber", 0, "serialNumber"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.4.13", "Description", 0, "description"));

        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.840.113549.1.1.1", "rsaEncryption", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.840.113549.1.1.5", "sha1WithRSAEncryption", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.3.6.1.5.5.7.1.1", "authorityInfoAccess", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.14", "X509v3 Subject Key Identifier", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.15", "X509v3 Key Usage", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.16", "X509v3 Private Key Usage Period", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.17", "X509v3 Subject Alternative Name", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.18", "X509v3 Issuer Alternative Name", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.19", "X509v3 Basic Constraints", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.30", "X509v3 Name Constraints", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.31", "X509v3 CRL Distribution Points", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.32", "X509v3 Certificate Policies Extension", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.33", "X509v3 Policy Mappings", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.35", "X509v3 Authority Key Identifier", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.36", "X509v3 Policy Constraints", 0));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("2.5.29.37", "X509v3 Extended Key Usage", 0));

        // TODO: Так и должно быть написано?
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.840.113549.1.7.3", "КонтейнерЗашифрованногоБлока", 0));

        // Hash algorithms
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.9", "GOST R 34.11-94", 0, null, "hash"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.2.1", "GOST R 34.11-94", 0, null, "hash"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.2.2", "GOST R 34.11-2012-256", 0, null, "hash"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.2.3", "GOST R 34.11-2012-512", 0, null, "hash"));

        // Encryption algorithms
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.21", "GOST 28147-89", 0, null, "dec"));

        // Key import/export algorithms
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.19", "GOST R 34.10-2001", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.20", "GOST R 34.10-94", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.1.1", "GOST R 34.10-2012-256", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.1.2", "GOST R 34.10-2012-512", 0, null, "X509"));

        // Signature algorithms
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.3.2", "GOST R 34.10-2012-256 + GOST R 34.11-2012-256", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.7.1.1.3.3", "GOST R 34.10-2012-512 + GOST R 34.11-2012-512", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.3", "GOST R 34.10-2001", 0, null, "X509"));
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.2.2.4", "GOST R 34.10-94", 0, null, "X509"));

        // TODO: Так и должно быть написано?
        OID_ATTRIBUTES.add(new CryptoDSSASNAttribute("1.2.643.100.111", "СредствоЭлектроннойПодписи", 0, null, "X509"));
    }

    /**
     * Returns supported OIDs objects.
     * Also, can be used to analyze the owner's existing certificate fields, signature, encrypted message or else.
     *
     * @return attributes with key of <code>oid</code> value and value of attribute.
     */
    // Alias: ОпределитьТип_OID
    public static Map<String, CryptoDSSASNAttribute> defineOIDType() {
        return defineOIDType(null);
    }

    /**
     * Returns supported OIDs objects.
     * Also, can be used to analyze the owner's existing certificate fields, signature, encrypted message or else.
     *
     * @param keyField key of returning attribute map.
     *
     * @return attributes with key of <code>oid</code> value and value of attribute.
     */
    // Alias: ОпределитьТип_OID
    public static Map<String, CryptoDSSASNAttribute> defineOIDType(String keyField) {
        return defineOIDType(keyField, "");
    }

    /**
     * Returns supported OIDs objects.
     * Also, can be used to analyze the owner's existing certificate fields, signature, encrypted message or else.
     *
     * @param keyField key of returning attribute map.
     * @param filterCategory filter assignment to filter unspecified attributes. Predefined values:
     *                          <ul>
     *                          <li><code>10</code> - owner's certificate fields or certificate request,</li>
     *                          <li><code>hash</code> - hash algorithms,</li>
     *                          <li><code>dec</code> - encryption algorithms,</li>
     *                          <li><code>X509</code> - signature algorithms.</li>
     *                          </ul>
     *
     * @return attributes with key of <code>oid</code> value and value of attribute.
     */
    // Alias: ОпределитьТип_OID
    public static Map<String, CryptoDSSASNAttribute> defineOIDType(String keyField, String filterCategory) {
        if (keyField == null || keyField.isEmpty()) {
            keyField = "oid";
        }

        Map<String, CryptoDSSASNAttribute> result = new HashMap<>();

        for (CryptoDSSASNAttribute attribute : OID_ATTRIBUTES) {
            if ((filterCategory == null || filterCategory.isEmpty()) ||
                    attribute.getDescription().equals(filterCategory)) {
                result.put(attribute.get(keyField), attribute);
            }
        }

        return result;
    }
}
