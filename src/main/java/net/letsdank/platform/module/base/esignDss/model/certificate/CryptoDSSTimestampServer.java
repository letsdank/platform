package net.letsdank.platform.module.base.esignDss.model.certificate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoDSSTimestampServer {
    /**
     * Name of the server.
     */
    private String name;

    /**
     * Web address of the timestamp server.
     */
    private String address;
}
