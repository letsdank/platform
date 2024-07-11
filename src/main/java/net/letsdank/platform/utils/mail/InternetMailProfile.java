package net.letsdank.platform.utils.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.service.email.settings.EmailProfile;

// Платформенный класс, для работы с почтой
@NoArgsConstructor
@Getter
@Setter
public class InternetMailProfile {
    private String imapServerAddress;
    private boolean useSSLForImap;
    private String imapPassword;
    private String imapUsername;
    private Integer imapPort;

    private String pop3ServerAddress;
    private boolean useSSLForPop3;
    private String password;
    private String username;
    private Integer pop3Port;

    private boolean pop3BeforeSmtp;
    private String smtpServerAddress;
    private boolean useSSLForSmtp;
    private String smtpPassword;
    private String smtpUsername;
    private Integer smtpPort;

    private int timeout;
}
