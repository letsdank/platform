package net.letsdank.platform.module.base.esignDss.model.certificate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoDSSProviders {
    /**
     * GUID of the crypto provider on DSS server.
     */
    private String id;

    /**
     * Name of the crypto provider.
     */
    private String description;

    /**
     * Type of the crypto provider.
     */
    private int type;

    /**
     * Name of the crypto provider.
     */
    private String name;
}
