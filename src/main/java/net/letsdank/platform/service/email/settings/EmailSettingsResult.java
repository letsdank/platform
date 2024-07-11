package net.letsdank.platform.service.email.settings;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// TODO: Обозвать по-другому
public class EmailSettingsResult {
    // TODO: Эти поля все нужны, осталось только их закомментировать
    private String usernameForReceiving;
    private String usernameForSending;

    private String passwordForReceiving;
    private String passwordForSending;

    private String incomingMailServer;
    private Integer incomingMailServerPort;
    private String outgoingMailServer;
    private Integer outgoingMailServerPort;

    private Boolean useSecureConnectionForIncomingMail;
    private Boolean useSecureConnectionForOutgoingMail;

    private Boolean forReceiving;
    private Boolean forSending;
    private Boolean settingsCheckPerformed;

    private String protocol;
}
