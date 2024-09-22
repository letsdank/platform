package net.letsdank.platform.module.base.esignDss.model.asn;

public enum CryptoDSSASNTag {
    OPTIONAL(0),
    BOOLEAN(1),
    INTEGER(2),
    BITSTRING(3),
    OCTETSTRING(4),
    NULL(5),
    OBJECT(6),
    OBJECTDISTRIPTION(7),
    INSTANCEOF(8),
    REAL(9),
    ENUMERATED(10),
    EMBEDDEDPVD(11),
    UTF8(12),
    RELATIVEOID(13),
    SEQUENCE(16),
    SET(17),
    NUMERICSTRING(18),
    PRINTABLESTRING(19),
    TELETEXSTRING(20),
    VIDEOSTRING(21),
    IA5STRING(22),
    UTCTIME(23),
    GENERALIZEDTIME(24),
    GRAFICSTRING(25),
    VISIBLESTRING(26),
    GENERALSTRING(27),
    UNIVERSALSTRING(28),
    CHARACTERSTRING(29),
    UNICODE(30),
    BITMAPSTRING(35);

    private final int value;

    CryptoDSSASNTag(final int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
