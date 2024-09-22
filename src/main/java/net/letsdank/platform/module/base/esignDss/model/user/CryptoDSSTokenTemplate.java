package net.letsdank.platform.module.base.esignDss.model.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
// Alias: НастройкиРаботыШаблонТокена
public class CryptoDSSTokenTemplate {
    /**
     * Base64 that service returns.
     */
    private String token = "";

    /**
     * Token type, most of <code>Bearer</code>.
     */
    private String tokenType = "";

    /**
     * Update token identifier.
     */
    private String updateToken = "";

    /**
     * Token expiration datetime.
     */
    private LocalDateTime expirationDate = null;
}
