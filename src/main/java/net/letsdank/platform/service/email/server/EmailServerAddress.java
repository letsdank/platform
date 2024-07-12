package net.letsdank.platform.service.email.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.utils.mail.InternetMailProtocol;

@NoArgsConstructor
@Getter
@Setter
public class EmailServerAddress {
    private InternetMailProtocol protocol;
    private String host;
    private int port;
    private EmailServerAddressEncryption encryption;
    private String loginFormat;
    private String smtpAuthentication;
    private boolean mustBeEnabled;
}
