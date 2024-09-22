package net.letsdank.platform.module.base.esignDss.model.asn;

// Alias: ОпределитьТип_Классы
public enum CryptoDSSASNClass {
    U(0),
    A(64),
    C(128),
    P(192);

    private final int value;

    CryptoDSSASNClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
