package net.letsdank.platform.service.email.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class EmailConnectionSettingsSMTP {
    private String outgoingServer;
    private Integer outgoingServerPort;
    private boolean useSecureConnectionForOutgoingMail;

    public static final EmailConnectionSettingsSMTP EMAIL_CONNECTION_ICLOUD =
            new EmailConnectionSettingsSMTP("smtp.mail.me.com", 465, false);
    public static final EmailConnectionSettingsSMTP EMAIL_CONNECTION_OUTLOOK =
            new EmailConnectionSettingsSMTP("smtp-mail.outlook.com", 587, false);

    public static List<EmailConnectionSettingsSMTP> getOtherSettings(String serverAddress) {
        return Arrays.asList(
                // Стандартные настройки, подходящие для ящиков gmail, yandex и mail.ru
                // Защищенное соединение, порт 465
                new EmailConnectionSettingsSMTP("smtp." + serverAddress, 465, true),
                new EmailConnectionSettingsSMTP("mail." + serverAddress, 465, true),
                new EmailConnectionSettingsSMTP(serverAddress, 465, true),
                // Защищенное (STARTTLS) соединение, порт 587
                new EmailConnectionSettingsSMTP("smtp." + serverAddress, 587, false),
                new EmailConnectionSettingsSMTP("mail." + serverAddress, 587, false),
                new EmailConnectionSettingsSMTP(serverAddress, 587, false),
                // Незащищенное соединение
                new EmailConnectionSettingsSMTP("smtp." + serverAddress, 25, false),
                new EmailConnectionSettingsSMTP("mail." + serverAddress, 25, false),
                new EmailConnectionSettingsSMTP(serverAddress, 25, false)
        );
    }
}
