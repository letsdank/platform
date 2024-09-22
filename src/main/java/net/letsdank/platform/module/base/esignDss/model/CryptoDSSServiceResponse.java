package net.letsdank.platform.module.base.esignDss.model;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.base.esignDss.CryptoDSSService;

@Getter
@Setter
public class CryptoDSSServiceResponse {
    /**
     * Boolean of successful call run.
     * If <code>true</code>, then usually the response is adding with <code>result</code> field.
     */
    private boolean done;

    /**
     * Marks if auth token was updated while job runs.
     * If <code>true</code>, the response is adding with <code>userSettings</code> field.
     */
    private boolean updatedMarker = false;

    /**
     * Contains formatted error description, if <code>done = false</code>.
     */
    private String error = "";

    /**
     * Contains error code.
     */
    private String errorCode = "";

    /**
     * @see CryptoDSSServiceResponse
     */
    private CryptoDSSErrorStatus errorStatus = new CryptoDSSErrorStatus();


    public CryptoDSSServiceResponse() {
        this(true);
    }

    public CryptoDSSServiceResponse(boolean done) {
        this.done = done;
    }
}
