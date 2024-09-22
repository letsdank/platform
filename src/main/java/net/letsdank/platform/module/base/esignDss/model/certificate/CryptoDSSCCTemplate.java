package net.letsdank.platform.module.base.esignDss.model.certificate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoDSSCCTemplate {
    private String name;

    /**
     * OID of the template.
     */
    private String template;
}
