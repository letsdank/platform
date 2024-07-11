package net.letsdank.platform.service.email.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailProfile implements Cloneable {
    private String usernameForReceiving;
    private String usernameForSending;

    private String passwordForSending;
    private String passwordForReceiving;

    private String protocol; // TODO: Переделать в Enum
    private String incomingMailServer;
    private Integer incomingMailServerPort;
    private boolean useSecureConnectionForIncomingMail;

    private String outgoingMailServer;
    private Integer outgoingMailServerPort;
    private boolean useSecureConnectionForOutgoingMail;
    private boolean requireLoginBeforeSending;

    private Integer serverTimeout;
    private boolean leaveCopiesOfEmailsOnServer;
    private Integer deleteEmailsFromServerAfter;

    // Alias: НастройкиПоУмолчанию
    public static EmailProfile getDefault(String emailAddress, String password) {
        int position = emailAddress.indexOf("@");
        String serverName = emailAddress.substring(position + 1);

        EmailProfile profile = new EmailProfile();
        profile.usernameForReceiving = emailAddress;
        profile.usernameForSending = emailAddress;
        profile.passwordForSending = password;
        profile.passwordForReceiving = password;

        profile.protocol = "IMAP";
        profile.incomingMailServer = "imap." + serverName;
        profile.incomingMailServerPort = 993;
        profile.useSecureConnectionForIncomingMail = true;

        profile.outgoingMailServer = "smtp." + serverName;
        profile.outgoingMailServerPort = 465;
        profile.useSecureConnectionForOutgoingMail = true;
        profile.requireLoginBeforeSending = false;

        profile.serverTimeout = 30;
        profile.leaveCopiesOfEmailsOnServer = true;
        profile.deleteEmailsFromServerAfter = 0;

        return profile;
    }

    @Override
    public EmailProfile clone() {
        try {
            return (EmailProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
