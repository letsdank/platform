package net.letsdank.platform.service.email.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class EmailConnectionSettingsIMAP {
    private String incomingServer;
    private Integer incomingServerPort;
    private boolean useSecureConnectionForIncomingMail;

    public static final EmailConnectionSettingsIMAP EMAIL_CONNECTION_ICLOUD =
            new EmailConnectionSettingsIMAP("imap.mail.me.com", 993, true);
    public static final EmailConnectionSettingsIMAP EMAIL_CONNECTION_OUTLOOK =
            new EmailConnectionSettingsIMAP("outlook.office365.com", 993, true);

    public static List<EmailConnectionSettingsIMAP> getOtherSettings(String serverAddress) {
        return Arrays.asList(
                // Стандартные настройки, подходящие для ящиков gmail, yandex и mail.ru
                // Защищенное соединение, порт 465
                new EmailConnectionSettingsIMAP("imap." + serverAddress, 993, true),
                new EmailConnectionSettingsIMAP("mail." + serverAddress, 993, true),
                new EmailConnectionSettingsIMAP(serverAddress, 993, true),
                // Незащищенное соединение
                new EmailConnectionSettingsIMAP("imap." + serverAddress, 143, false),
                new EmailConnectionSettingsIMAP("mail." + serverAddress, 143, false),
                new EmailConnectionSettingsIMAP(serverAddress, 143, false)
        );
    }
}
