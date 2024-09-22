package net.letsdank.platform.module.base.esignDss.model.certificate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoDSSCCField {
    /**
     * ID of the field.
     */
    private String oid;

    /**
     * <code>true</code>, if this field is required.
     */
    private boolean required;
}
