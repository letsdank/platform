package net.letsdank.platform.module.base.esignDss.model.certificate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CryptoDSSCertificationCenter {
    /**
     * Name of the center.
     */
    private String name;

    /**
     * Internal ID of the center in the DSS server.
     */
    private String id;

    /**
     * Is the center an external center that requires separated installation?
     */
    private boolean external;

    /**
     * List of templates for this center.
     */
    private List<CryptoDSSCCTemplate> templates;

    /**
     * List of fields for this center.
     */
    private List<CryptoDSSCCField> fields;
}
