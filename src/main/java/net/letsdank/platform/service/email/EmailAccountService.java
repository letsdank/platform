package net.letsdank.platform.service.email;

import net.letsdank.platform.service.email.server.EmailMailServerSettings;
import net.letsdank.platform.service.email.settings.*;
import net.letsdank.platform.utils.mail.InternetMail;
import net.letsdank.platform.utils.mail.InternetMailMessage;
import net.letsdank.platform.utils.mail.InternetMailProfile;
import net.letsdank.platform.utils.mail.InternetMailProtocol;
import net.letsdank.platform.utils.string.StringUtils;
import net.letsdank.platform.utils.url.URLInfo;
import net.letsdank.platform.utils.url.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EmailAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAccountService.class);


    // Alias: ОпределитьИменаПочтовыхСерверовДомена
    public static List<String> determineMailServersForDomain(String domain) {
        List<String> result = new ArrayList<>();

        List<String> dnsServers = EmailService.getDnsServers();
        dnsServers.add(0, ""); // Add default DNS server.

        for (String dnsServer : dnsServers) {
            String command = StringUtils.substituteParameters("nslookup -type=mx %1 %2",
                    domain, dnsServer);

            try {
                // TODO: Выписать в отдельный функциональный класс (ФайловаяСистема)
                Process process = Runtime.getRuntime().exec(command);

                BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                StringBuilder output = new StringBuilder();
                while ((line = outputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                while ((line = errorReader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                if (process.waitFor() == 0) {
                    String[] lines = output.toString().split("\n");
                    for (String lineStr : lines) {
                        if (lineStr.contains("mail exchanger")) {
                            String[] parts = lineStr.split(" ");
                            String serverName = parts[parts.length - 1];
                            result.add(serverName);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                // ignore
            }

            if (!result.isEmpty()) {
                break;
            }
        }

        return result;
    }

    // Alias: ОпределитьНастройкиУчетнойЗаписи
    public static EmailSettingsResult determineEmailSettings(String email, String password,
                                                             boolean forSending, boolean forReceiving) {
        email = EmailService.convertToPunycode(email);
        EmailConnectionSettings foundSettings = getEmailSettingsByEmail(email, password);

        return pickEmailSettings(email, password, forSending, forReceiving, foundSettings.getProfile());
    }

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public static EmailConnectionSettings getEmailSettingsByEmail(String email) {
        return getEmailSettingsByEmail(email, "");
    }

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public static EmailConnectionSettings getEmailSettingsByEmail(String email, String password) {
        URLInfo info = URLUtils.getURLInfo(email);
        String mailDomain = info.getHost();

        Object foundSettings = null;
        String mailServerName = "";

        EmailMailServerSettings mailServersSettings = getMailServerSettings();
        // Если в настройках ключ ведет к алиасу, берем данные через алиас на основной сервер
        if (mailServersSettings.isAlias(mailDomain)) {
            mailServerName = mailServersSettings.getAlias(mailDomain);
            foundSettings = mailServersSettings.getServer(mailServerName);
        } else {
            foundSettings = mailServersSettings.getServer(mailDomain);
            mailServerName = mailDomain;
        }

        if (foundSettings == null) {
            List<String> serverNames = determineMailServersForDomain(mailDomain);
            for (String serverName : serverNames) {
                String[] domainLevels = serverName.split("\\.");
                while (domainLevels.length > 1) {
                    serverName = String.join(".", domainLevels);
                    if (mailServersSettings.isAlias(serverName)) {
                        mailServerName = mailServersSettings.getAlias(serverName);
                        foundSettings = mailServersSettings.getServer(mailServerName);
                    } else {
                        foundSettings = mailServersSettings.getServer(serverName);
                        mailServerName = serverName;
                    }

                    if (foundSettings != null) break;
                    domainLevels = Arrays.copyOfRange(domainLevels, 1, domainLevels.length);
                }
                if (foundSettings != null) break;
            }
        }

        InternetMailProfile profile = null;
        if (foundSettings != null) {
            profile = formProfile(foundSettings, email, password);
        }

        Object authorizationSettings = getAuthorizationSettingsForServer
                (foundSettings, mailServerName, mailDomain);

        return new EmailConnectionSettings(profile, mailServerName, authorizationSettings);
    }

    private static Object getAuthorizationSettingsForServer(Object settings, String serverName, String domain) {
        return null; // TODO: Implement
    }

    private static EmailMailServerSettings getMailServerSettings() {
        return new EmailMailServerSettings(); // TODO: Implement
    }

    // Alias: ПодобратьНастройкиПочты
    private static EmailSettingsResult pickEmailSettings(String email, String password, boolean forSending, boolean forReceiving) {
        return pickEmailSettings(email, password, forSending, forReceiving, null);
    }

    // Alias: ПодобратьНастройкиПочты
    private static EmailSettingsResult pickEmailSettings(String email, String password, boolean forSending, boolean forReceiving, InternetMailProfile profile) {
        boolean settingsReceived = profile != null;

        InternetMailProfile foundSmtpProfile = (settingsReceived && profile.getSmtpServerAddress() != null) ? profile : null;
        InternetMailProfile foundImapProfile = (settingsReceived && profile.getImapServerAddress() != null) ? profile : null;

        if (!settingsReceived) {
            if (forSending) {
                foundSmtpProfile = determineSmtpSettings(email, password);
            }

            if (forSending || forReceiving) {
                foundImapProfile = determineImapSettings(email, password);
            }
        }

        EmailSettingsResult result = new EmailSettingsResult();

        if (foundImapProfile != null) {
            result.setUsernameForReceiving(foundImapProfile.getImapUsername());
            result.setPasswordForReceiving(foundImapProfile.getImapPassword());
            result.setProtocol("SMTP");
            result.setIncomingMailServer(foundImapProfile.getImapServerAddress());
            result.setIncomingMailServerPort(foundImapProfile.getImapPort());
            result.setUseSecureConnectionForIncomingMail(foundImapProfile.isUseSSLForImap());
        }

        if (foundSmtpProfile != null) {
            result.setUsernameForSending(foundSmtpProfile.getSmtpUsername());
            result.setPasswordForSending(foundSmtpProfile.getSmtpPassword());
            result.setOutgoingMailServer(foundSmtpProfile.getSmtpServerAddress());
            result.setOutgoingMailServerPort(foundSmtpProfile.getSmtpPort());
            result.setUseSecureConnectionForOutgoingMail(foundSmtpProfile.isUseSSLForSmtp());
        }

        result.setForReceiving(foundImapProfile != null);
        result.setForSending(foundSmtpProfile != null);
        result.setSettingsCheckPerformed(!settingsReceived);

        return result;
    }

    // Alias: СформироватьПрофиль
    private static InternetMailProfile formProfile(Object settings, String email, String password) {
        return null; // TODO: Implement
    }

    // Alias: ОпределитьНастройкиIMAP
    private static InternetMailProfile determineImapSettings(String emailAddress, String password) {
        for (InternetMailProfile profile : getImapProfiles(emailAddress, password)) {
            String serverMessage = checkIncomingMailServerConnection(profile, InternetMailProtocol.IMAP);

            if (isAuthenticationError(serverMessage)) {
                for (String username : getUsernameVariants(emailAddress)) {
                    setUsername(profile, username);
                    serverMessage = checkIncomingMailServerConnection(profile, InternetMailProtocol.IMAP);
                    if (!isAuthenticationError(serverMessage)) break;
                }
                if (isAuthenticationError(serverMessage)) break;
            }

            if (isConnectionEstablished(serverMessage)) {
                return profile;
            }
        }
        return null;
    }

    // Alias: ОпределитьНастройкиSMTP
    private static InternetMailProfile determineSmtpSettings(String emailAddress, String password) {
        for (InternetMailProfile profile : getSmtpProfiles(emailAddress, password)) {
            String serverMessage = checkOutgoingMailServerConnection(profile, emailAddress);

            if (isAuthenticationError(serverMessage)) {
                for (String username : getUsernameVariants(emailAddress)) {
                    setUsername(profile, username);
                    serverMessage = checkOutgoingMailServerConnection(profile, emailAddress);
                    if (!isAuthenticationError(serverMessage)) break;
                }
                if (isAuthenticationError(serverMessage)) break;
            }
            if (isConnectionEstablished(serverMessage)) {
                return profile;
            }
        }
        return null;
    }

    // Alias: ОшибкаАутентификации
    private static boolean isAuthenticationError(String serverMessage) {
        return serverMessage.toLowerCase().indexOf("auth") > 0 ||
                serverMessage.toLowerCase().indexOf("password") > 0 ||
                serverMessage.toLowerCase().indexOf("credentials") > 0;
    }

    // Alias: ПодключениеВыполнено
    private static boolean isConnectionEstablished(String serverMessage) {
        return serverMessage.isEmpty();
    }

    // Alias: УстановитьИмяПользователя
    private static void setUsername(InternetMailProfile profile, String username) {
        if (!profile.getUsername().isEmpty()) profile.setUsername(username);
        if (!profile.getImapUsername().isEmpty()) profile.setImapUsername(username);
        if (!profile.getSmtpUsername().isEmpty()) profile.setSmtpUsername(username);
    }

    // Alias: ПроверитьПодключениеКСерверуВходящейПочты
    private static String checkIncomingMailServerConnection(InternetMailProfile profile, InternetMailProtocol protocol) {
        InternetMail mail = new InternetMail();
        String errorMessage = "";

        try {
            mail.connect(profile, protocol);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        mail.disconnect();

        // TODO: Записывать в журнал регистрации
        if (protocol == InternetMailProtocol.POP3) {
            LOGGER.info("{}:{}{} ({})\n{}",
                    profile.getPop3ServerAddress(),
                    profile.getPop3Port(),
                    profile.isUseSSLForPop3() ? "/SSL" : "",
                    profile.getUsername(),
                    errorMessage.isEmpty() ? "OK" : errorMessage);
        } else {
            LOGGER.info("{}:{}{} ({})\n{}",
                    profile.getImapServerAddress(),
                    profile.getImapPort(),
                    profile.isUseSSLForImap() ? "/SSL" : "",
                    profile.getImapUsername(),
                    errorMessage.isEmpty() ? "OK" : errorMessage);
        }

        return errorMessage;
    }

    // Alias: ПроверитьПодключениеКСерверуИсходящейПочты
    private static String checkOutgoingMailServerConnection(InternetMailProfile profile, String emailAddress) {
        String subject = "Тестовое сообщение Platform";
        String body = "Это сообщение отправлено подсистемой электронной почты Platform";
        String fromName = "Platform";

        InternetMailMessage message = new InternetMailMessage();
        message.setSubject(subject);
        message.addTo(emailAddress, fromName);
        message.setFrom(emailAddress, fromName);
        message.addBody(body, "simpleText"); // TODO

        InternetMail mail = new InternetMail();
        String errorMessage = "";

        try {
            mail.connect(profile);
            mail.sendMessage(message);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        mail.disconnect();

        // TODO: Записывать в журнал регистрации
        LOGGER.info("{}:{}{} ({})\n{}",
                profile.getSmtpServerAddress(),
                profile.getSmtpPort(),
                profile.isUseSSLForSmtp() ? "/SSL" : "",
                profile.getSmtpUsername(),
                errorMessage.isEmpty() ? "OK" : errorMessage);

        return errorMessage;
    }

    // Alias: ВариантыИмениПользователя
    private static List<String> getUsernameVariants(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String username = emailAddress.substring(pos - 1);

        return List.of(username);
    }

    // Alias: ПрофилиIMAP
    private static List<InternetMailProfile> getImapProfiles(String emailAddress, String password) {
        List<InternetMailProfile> result = new ArrayList<>();
        EmailProfile initialProfile = EmailProfile.getDefault(emailAddress, password);

        for (EmailConnectionSettingsIMAP connectionSettings : getImapConnectionSettings(emailAddress)) {
            EmailProfile profile = initialProfile.clone();
            profile.setIncomingMailServer(connectionSettings.getIncomingServer());
            profile.setIncomingMailServerPort(connectionSettings.getIncomingServerPort());
            profile.setUseSecureConnectionForIncomingMail(connectionSettings.isUseSecureConnectionForIncomingMail());

            result.add(getInternetMailProfile(profile, InternetMailProtocol.IMAP));
        }

        return result;
    }

    // Alias: ПрофилиSMTP
    private static List<InternetMailProfile> getSmtpProfiles(String emailAddress, String password) {
        List<InternetMailProfile> result = new ArrayList<>();
        EmailProfile initialProfile = EmailProfile.getDefault(emailAddress, password);

        for (EmailConnectionSettingsSMTP connectionSettings : getSmtpConnectionSettings(emailAddress)) {
            EmailProfile profile = initialProfile.clone();
            profile.setOutgoingMailServer(connectionSettings.getOutgoingServer());
            profile.setOutgoingMailServerPort(connectionSettings.getOutgoingServerPort());
            profile.setUseSecureConnectionForOutgoingMail(connectionSettings.isUseSecureConnectionForOutgoingMail());
            result.add(getInternetMailProfile(profile, InternetMailProtocol.SMTP));
        }

        return result;
    }

    // Alias: ИнтернетПочтовыйПрофиль
    private static InternetMailProfile getInternetMailProfile(EmailProfile profile, InternetMailProtocol protocol) {
        boolean forReceiving = protocol != InternetMailProtocol.SMTP;

        InternetMailProfile mailProfile = new InternetMailProfile();
        if (forReceiving || profile.isRequireLoginBeforeSending()) {
            if (protocol == InternetMailProtocol.IMAP) {
                mailProfile.setImapServerAddress(profile.getIncomingMailServer());
                mailProfile.setUseSSLForImap(profile.isUseSecureConnectionForIncomingMail());
                mailProfile.setImapPassword(profile.getPasswordForReceiving());
                mailProfile.setImapUsername(profile.getUsernameForReceiving());
                mailProfile.setImapPort(profile.getIncomingMailServerPort());
            } else {
                mailProfile.setPop3ServerAddress(profile.getIncomingMailServer());
                mailProfile.setUseSSLForPop3(profile.isUseSecureConnectionForIncomingMail());
                mailProfile.setPassword(profile.getPasswordForReceiving());
                mailProfile.setUsername(profile.getUsernameForReceiving());
                mailProfile.setPop3Port(profile.getIncomingMailServerPort());
            }
        }

        if (!forReceiving) {
            mailProfile.setPop3BeforeSmtp(profile.isRequireLoginBeforeSending());
            mailProfile.setSmtpServerAddress(profile.getOutgoingMailServer());
            mailProfile.setUseSSLForSmtp(profile.isUseSecureConnectionForOutgoingMail());
            mailProfile.setSmtpPassword(profile.getPasswordForSending());
            mailProfile.setSmtpUsername(profile.getUsernameForSending());
            mailProfile.setSmtpPort(profile.getOutgoingMailServerPort());
        }

        mailProfile.setTimeout(profile.getServerTimeout());
        return mailProfile;
    }

    // Alias: ВариантыНастройкиПодключенияКСерверуIMAP
    public static List<EmailConnectionSettingsIMAP> getImapConnectionSettings(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String serverAddress = emailAddress.substring(pos + 1);

        if (serverAddress.equals("icloud.com"))
            return List.of(EmailConnectionSettingsIMAP.EMAIL_CONNECTION_ICLOUD);
        if (serverAddress.equals("outlook.com"))
            return List.of(EmailConnectionSettingsIMAP.EMAIL_CONNECTION_OUTLOOK);

        return EmailConnectionSettingsIMAP.getOtherSettings(serverAddress);
    }

    // Alias: ВариантыНастройкиПодключенияКСерверуSMTP
    public static List<EmailConnectionSettingsSMTP> getSmtpConnectionSettings(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String serverAddress = emailAddress.substring(pos + 1);

        if (serverAddress.equals("icloud.com"))
            return List.of(EmailConnectionSettingsSMTP.EMAIL_CONNECTION_ICLOUD);
        if (serverAddress.equals("outlook.com"))
            return List.of(EmailConnectionSettingsSMTP.EMAIL_CONNECTION_OUTLOOK);

        return EmailConnectionSettingsSMTP.getOtherSettings(serverAddress);
    }
}
