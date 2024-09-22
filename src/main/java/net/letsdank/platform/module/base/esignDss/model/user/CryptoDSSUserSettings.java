package net.letsdank.platform.module.base.esignDss.model.user;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.entity.esignDss.DSSAccount;
import net.letsdank.platform.entity.esignDss.DSSAuthMethod;
import net.letsdank.platform.entity.esignDss.DSSServer;
import net.letsdank.platform.utils.data.Either;

import java.util.UUID;

/**
 * This class describes session data of user account.
 */
@Getter
@Setter
// Alias: НастройкиПользователяПоУмолчанию
public class CryptoDSSUserSettings {
    /**
     * Version of DSS server.
     */
    private String apiVersion = "2.0.2882";

    /**
     * Privacy data forming sign.
     */
    private int privacySign = 1;

    /**
     * DSS account of this session data.
     */
    private DSSAccount account = null;

    /**
     * DSS server of this session data.
     */
    private DSSServer server = null;

    /**
     * Token template.
     */
    private CryptoDSSTokenTemplate authToken = new CryptoDSSTokenTemplate();

    /**
     * User account ID.
     */
    private UUID uuid = null;

    private String login = "";
    /**
     * User account ID from cloud signing server.
     */
    private String serverUuid;

    private Either<String, Object> password; // TODO:
    private Either<String, Object> certificatePassword; // TODO:

    /**
     * Information about containing auth certificate.
     */
    private boolean certificateAuth;

    /**
     * Phone number while authorization.
     */
    private String phone;

    /**
     * Email address while authorization.
     */
    private String email;

    /**
     * Primary auth method (4 types)
     */
    private DSSAuthMethod primaryAuth;

    /**
     * Primary operations auth method.
     */
    private DSSAuthMethod secondaryAuth;

    /**
     * @see CryptoDSSUserPolicySettings
     */
    private CryptoDSSUserPolicySettings policy;

    // TODO: Implement
}
