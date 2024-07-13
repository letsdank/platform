package net.letsdank.platform.service.email.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class EmailServerOAuthSettings {
    private String authorizationURI;
    private String deviceAuthorizationURI;
    private String tokenExchangeURI;
    private List<String> mailScope;
    private boolean usePKCE;
    private boolean useClientSecret;
    private Map<String, String> authorizationParameters;
    private Map<String, String> tokenExchangeParameters;
    private InternationalString redirectURIDescription;
    private InternationalString redirectURICaption;
    private String defaultRedirectURI;
    private InternationalString clientIDDescription;
    private InternationalString clientIDCaption;
    private InternationalString clientSecretDescription;
    private InternationalString clientSecretCaption;
    private InternationalString additionalDescription;
}
