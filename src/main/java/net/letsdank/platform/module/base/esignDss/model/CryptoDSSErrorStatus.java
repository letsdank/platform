package net.letsdank.platform.module.base.esignDss.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Alias: СтатусОшибки
public class CryptoDSSErrorStatus {
    /**
     * Formatted error description.
     */
    private String description = "";

    /**
     * Fixed error code in subsystem.
     */
    private String errorCode = "";

    /**
     * Error description from server response.
     */
    private String sourceDescription = "";

    /**
     * Error code from server response.
     */
    private String sourceCode = "";

    /**
     * Platform error description.
     */
    private String fullText = "";
}
