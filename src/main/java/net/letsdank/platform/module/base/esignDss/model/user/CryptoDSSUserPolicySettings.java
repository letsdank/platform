package net.letsdank.platform.module.base.esignDss.model.user;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.base.esignDss.model.certificate.CryptoDSSCertificationCenter;
import net.letsdank.platform.module.base.esignDss.model.certificate.CryptoDSSProviders;
import net.letsdank.platform.module.base.esignDss.model.certificate.CryptoDSSTimestampServer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to store user account policy settings.
 */
@Getter
@Setter
// Alias: НастройкиПолитикиПользователяПоУмолчанию
public class CryptoDSSUserPolicySettings {
    /**
     * Marked if authorization was successful and obtained user account settings from server.
     */
    private boolean filled = false;

    /**
     * Date when settings was filled.
     */
    private LocalDate filledDate = LocalDate.of(1, 1, 1);

    /**
     * Contains action list that allowed to user and accept mark.
     */
    private Map<String, CryptoDSSPolicyAction> actions = new HashMap<>();

    /**
     * Contains list of certification centers.
     */
    private List<CryptoDSSCertificationCenter> certificationCenters = new ArrayList<>();

    /**
     * List of providers for this center.
     */
    private List<CryptoDSSProviders> providers = new ArrayList<>();

    /**
     * List of timestamp servers for this center.
     */
    private List<CryptoDSSTimestampServer> timestampServers = new ArrayList<>();

    /**
     * List of IDs of signature types.
     */
    private List<Long> signatureTypes;

    /**
     * String constant of pin code applying mode for certificates.
     * Variants: <code>Required</code>, <code>Forbidden</code>, <code>Optional</code>.
     * // TODO: As enum
     */
    private String pinCodeType = "Optional";
}
