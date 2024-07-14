package net.letsdank.platform.service.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.letsdank.platform.module.email.EmailMessageImpl;
import net.letsdank.platform.module.net.InternetConnectionDiagnosticResult;
import net.letsdank.platform.module.net.InternetFileDownloader;
import net.letsdank.platform.service.auth.ServiceAuthenticationSettingsObject;
import net.letsdank.platform.service.auth.ServiceAuthenticationSettingsService;
import net.letsdank.platform.service.email.server.*;
import net.letsdank.platform.service.email.settings.*;
import net.letsdank.platform.utils.mail.InternetMail;
import net.letsdank.platform.utils.mail.InternetMailMessage;
import net.letsdank.platform.utils.mail.InternetMailProfile;
import net.letsdank.platform.utils.mail.InternetMailProtocol;
import net.letsdank.platform.utils.string.StringUtils;
import net.letsdank.platform.utils.string.XMLString;
import net.letsdank.platform.utils.url.URLInfo;
import net.letsdank.platform.utils.url.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAccountService.class);

    private final ServiceAuthenticationSettingsService serviceAuthenticationSettingsService;

    // Alias: ОпределитьИменаПочтовыхСерверовДомена
    public List<String> determineMailServersForDomain(String domain) {
        List<String> result = new ArrayList<>();

        List<String> dnsServers = EmailMessageImpl.getDnsServers();
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
    public EmailSettingsResult determineEmailSettings(String email, String password,
                                                             boolean forSending, boolean forReceiving) {
        email = EmailMessageImpl.convertToPunycode(email);
        EmailConnectionSettings foundSettings = getEmailSettingsByEmail(email, password);

        return pickEmailSettings(email, password, forSending, forReceiving, foundSettings.getProfile());
    }

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public EmailConnectionSettings getEmailSettingsByEmail(String email) {
        return getEmailSettingsByEmail(email, "");
    }

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public EmailConnectionSettings getEmailSettingsByEmail(String email, String password) {
        URLInfo info = URLUtils.getURLInfo(email);
        String mailDomain = info.getHost();

        EmailServerSettings foundSettings;
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

        if (foundSettings == null) {
            foundSettings = new EmailServerSettings();
        }

        InternetMailProfile profile = null;
        if (foundSettings != null) {
            profile = formProfile(foundSettings, email, password);
        }

        Object authorizationSettings = getAuthorizationSettingsForServer
                (foundSettings, mailServerName, mailDomain);

        return new EmailConnectionSettings(profile, mailServerName, authorizationSettings);
    }

    // Alias: НастройкиАвторизацииСервера
    private ServiceAuthenticationSettingsObject getAuthorizationSettingsForServer(
            EmailServerSettings settings, String serverName, String domain) {

        ServiceAuthenticationSettingsObject authenticationSettings =
                serviceAuthenticationSettingsService.getSettings(serverName, domain);
        EmailServerOAuthSettings classifierSettings = settings.getOauthSettings();

        if (classifierSettings == null) {
            return authenticationSettings;
        }

        authenticationSettings.setServiceName(serverName);
        authenticationSettings.setOwnerName(domain);

        if (authenticationSettings.getAuthorizationUri() == null) {
            authenticationSettings.setAuthorizationUri(classifierSettings.getAuthorizationURI());
        }

        if (authenticationSettings.getGrantKeyUri() == null) {
            authenticationSettings.setGrantKeyUri(classifierSettings.getTokenExchangeURI());
        }

        List<String> scopes = classifierSettings.getMailScope();
        authenticationSettings.setScopes(String.join(" ", scopes.toArray(new String[0])));
        authenticationSettings.setUsePkce(classifierSettings.isUsePKCE());
        authenticationSettings.setUseClientSecret(classifierSettings.isUseClientSecret());

        Map<String, String> additionalAuthorizationParameters = classifierSettings.getAuthorizationParameters();
        if (additionalAuthorizationParameters != null) {
            List<String> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : additionalAuthorizationParameters.entrySet()) {
                parameters.add(entry.getKey() + "=" + XMLString.convert(entry.getValue()));
            }
            authenticationSettings.setAdditionalAuthorizationParameters(
                    String.join(" ", parameters.toArray(new String[0])));
        }

        Map<String, String> additionalGrantParameters = classifierSettings.getTokenExchangeParameters();
        if (additionalGrantParameters != null) {
            List<String> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : additionalGrantParameters.entrySet()) {
                parameters.add(entry.getKey() + "=" + XMLString.convert(entry.getValue()));
            }
            authenticationSettings.setAdditionalGrantParameters(
                    String.join(" ", parameters.toArray(new String[0])));
        }

        authenticationSettings.setRedirectUriDescription(
                getStringForCurrentLanguage(classifierSettings.getRedirectURIDescription()));
        authenticationSettings.setClientIdDescription(
                getStringForCurrentLanguage(classifierSettings.getClientIDDescription()));
        authenticationSettings.setClientIdDescription(
                getStringForCurrentLanguage(classifierSettings.getClientIDDescription()));
        authenticationSettings.setAdditionalDescription(
                getStringForCurrentLanguage(classifierSettings.getAdditionalDescription()));

        authenticationSettings.setRedirectUriCaption(getStringForCurrentLanguage(classifierSettings.getRedirectURICaption()));
        authenticationSettings.setClientIdCaption(getStringForCurrentLanguage(classifierSettings.getClientIDCaption()));
        authenticationSettings.setClientSecretCaption(getStringForCurrentLanguage(classifierSettings.getClientSecretCaption()));

        authenticationSettings.setRedirectUri(classifierSettings.getDefaultRedirectURI());
        authenticationSettings.setRedirectUriWebClient(classifierSettings.getDefaultRedirectURI());

        authenticationSettings.setRegisterDeviceUri(classifierSettings.getDeviceAuthorizationURI());

        return authenticationSettings;
    }

    // Alias: СтрокаДляТекущегоЯзыка
    private String getStringForCurrentLanguage(InternationalString string) {
        if (string == null) return "";

        List<String> languages = Arrays.asList(LocaleContextHolder.getLocale().getLanguage(), "en");
        for (String lang : languages) {
            if (string.getValueFor(lang) != null)
                return string.getValueFor(lang);
        }

        return "";
    }

    // Alias: НастройкиПочтовыхСерверов
    private EmailMailServerSettings getMailServerSettings() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // TODO: Вынести в отдельный общий сервер-репозиторий
            ClassPathResource resource = new ClassPathResource("data/email/mail-servers.json");

            return mapper.readValue(resource.getInputStream(), EmailMailServerSettings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Alias: ПодобратьНастройкиПочты
    private EmailSettingsResult pickEmailSettings(String email, String password, boolean forSending, boolean forReceiving) {
        return pickEmailSettings(email, password, forSending, forReceiving, null);
    }

    // Alias: ПодобратьНастройкиПочты
    private EmailSettingsResult pickEmailSettings(String email, String password, boolean forSending, boolean forReceiving, InternetMailProfile profile) {
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
    private InternetMailProfile formProfile(EmailServerSettings settings, String emailAddress) {
        return formProfile(settings, emailAddress, "");
    }

    // Alias: СформироватьПрофиль
    private InternetMailProfile formProfile(EmailServerSettings settings, String emailAddress, String password) {
        if (settings.getServices().isEmpty()) return null;
        URLInfo info = URLUtils.getURLInfo(emailAddress);

        InternetMailProfile profile = new InternetMailProfile();
        for (EmailServerAddress address : settings.getServices()) {
            if (address.getProtocol() == InternetMailProtocol.IMAP && profile.getSmtpServerAddress() == null) {
                if ("Enabled".equals(address.getSmtpAuthentication()) || address.getSmtpAuthentication() == null) {
                    String username = "username".equals(address.getLoginFormat()) ? info.getLogin() : emailAddress;
                    profile.setSmtpUsername(username);
                    profile.setSmtpPassword(password);
                }

                profile.setUseSSLForSmtp(address.getEncryption() == EmailServerAddressEncryption.SSL);
                profile.setSmtpServerAddress(address.getHost());
                profile.setSmtpPort(address.getPort());
            }

            if (address.getProtocol() == InternetMailProtocol.IMAP && profile.getImapServerAddress() == null) {
                String username = "username".equals(address.getLoginFormat()) ? info.getLogin() : emailAddress;
                profile.setUseSSLForImap(address.getEncryption() == EmailServerAddressEncryption.SSL);
                profile.setImapUsername(username);
                profile.setImapPassword(password);
                profile.setImapServerAddress(address.getHost());
                profile.setImapPort(address.getPort());
            }
        }

        return profile;
    }

    // Alias: ОпределитьНастройкиIMAP
    private InternetMailProfile determineImapSettings(String emailAddress, String password) {
        // TODO: Сделать многопоточность, так как долго длится проверка по перебору
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
    private InternetMailProfile determineSmtpSettings(String emailAddress, String password) {
        // TODO: Сделать многопоточность, так как долго длится проверка по перебору
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
    private boolean isAuthenticationError(String serverMessage) {
        return serverMessage.toLowerCase().indexOf("auth") > 0 ||
                serverMessage.toLowerCase().indexOf("password") > 0 ||
                serverMessage.toLowerCase().indexOf("credentials") > 0;
    }

    // Alias: ПодключениеВыполнено
    private boolean isConnectionEstablished(String serverMessage) {
        return serverMessage.isEmpty();
    }

    // Alias: УстановитьИмяПользователя
    private void setUsername(InternetMailProfile profile, String username) {
        if (profile.getUsername() != null && !profile.getUsername().isEmpty()) profile.setUsername(username);
        if (profile.getImapUsername() != null && !profile.getImapUsername().isEmpty()) profile.setImapUsername(username);
        if (profile.getSmtpUsername() != null && !profile.getSmtpUsername().isEmpty()) profile.setSmtpUsername(username);
    }

    // Alias: ПроверитьПодключениеКСерверуВходящейПочты
    private String checkIncomingMailServerConnection(InternetMailProfile profile, InternetMailProtocol protocol) {
        InternetMail mail = new InternetMail();
        String errorMessage = "";

        try {
            mail.connect(profile, protocol);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            if (errorMessage == null) errorMessage = "";
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
    private String checkOutgoingMailServerConnection(InternetMailProfile profile, String emailAddress) {
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
            if (errorMessage == null) errorMessage = "";
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
    private List<String> getUsernameVariants(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String username = emailAddress.substring(pos - 1);

        return List.of(username);
    }

    // Alias: ПрофилиIMAP
    private List<InternetMailProfile> getImapProfiles(String emailAddress, String password) {
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
    private List<InternetMailProfile> getSmtpProfiles(String emailAddress, String password) {
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
    private InternetMailProfile getInternetMailProfile(EmailProfile profile, InternetMailProtocol protocol) {
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
    public List<EmailConnectionSettingsIMAP> getImapConnectionSettings(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String serverAddress = emailAddress.substring(pos + 1);

        if (serverAddress.equals("icloud.com"))
            return List.of(EmailConnectionSettingsIMAP.EMAIL_CONNECTION_ICLOUD);
        if (serverAddress.equals("outlook.com"))
            return List.of(EmailConnectionSettingsIMAP.EMAIL_CONNECTION_OUTLOOK);

        return EmailConnectionSettingsIMAP.getOtherSettings(serverAddress);
    }

    // Alias: ВариантыНастройкиПодключенияКСерверуSMTP
    public List<EmailConnectionSettingsSMTP> getSmtpConnectionSettings(String emailAddress) {
        int pos = emailAddress.indexOf("@");
        String serverAddress = emailAddress.substring(pos + 1);

        if (serverAddress.equals("icloud.com"))
            return List.of(EmailConnectionSettingsSMTP.EMAIL_CONNECTION_ICLOUD);
        if (serverAddress.equals("outlook.com"))
            return List.of(EmailConnectionSettingsSMTP.EMAIL_CONNECTION_OUTLOOK);

        return EmailConnectionSettingsSMTP.getOtherSettings(serverAddress);
    }

    // Alias: ПроверитьНастройкиПрофилей
    public EmailCheckerResult checkEmailProfile(InternetMailProfile outgoingProfile,
                                                InternetMailProfile incomingProfile,
                                                String emailAddress) {
        EmailCheckerResult result = new EmailCheckerResult();

        boolean authError = false;
        boolean needToCheckOutgoingProfile = false;
        boolean needToCheckIncomingProfile = false;

        if (outgoingProfile != null) {
            String error = checkOutgoingMailServerConnection(outgoingProfile, emailAddress);
            if (!error.isEmpty()) {
                result.addErrorMessage(error);
                result.addTechnicalDetails(error);

                authError = isAuthenticationError(error);
                if (authError) {
                    result.addErrorMessage("Failed to send test message: authentication failed.");
                } else {
                    result.addErrorMessage("Failed to send test message.");
                    needToCheckOutgoingProfile = true;

                    InternetConnectionDiagnosticResult diagnosticResult = InternetFileDownloader.diagnoseConnection(
                            outgoingProfile.getSmtpServerAddress());
                    result.addTechnicalDetails(diagnosticResult.getDiagnosticLog());
                }
            } else {
                result.addExecutedCheck("- Outgoing profile check passed.");
            }
        }

        if (incomingProfile != null) {
            InternetMailProtocol protocol = incomingProfile.getSmtpServerAddress() != null ?
                    InternetMailProtocol.IMAP : InternetMailProtocol.POP3;

            String error = checkIncomingMailServerConnection(incomingProfile, protocol);
            if (!error.isEmpty()) {
                result.addErrorMessage(error);
                result.addTechnicalDetails(error);

                authError = isAuthenticationError(error);
                if (authError) {
                    result.addErrorMessage("Failed to connect to incoming mail server: authentication failed.");
                } else {
                    result.addErrorMessage("Failed to connect to incoming mail server.");
                    needToCheckIncomingProfile = true;

                    String serverAddress = protocol == InternetMailProtocol.IMAP ?
                            incomingProfile.getImapServerAddress() : incomingProfile.getPop3ServerAddress();
                    InternetConnectionDiagnosticResult diagnosticResult = InternetFileDownloader.diagnoseConnection(serverAddress);
                    result.addTechnicalDetails(diagnosticResult.getDiagnosticLog());
                }
            } else {
                result.addExecutedCheck("- Incoming profile check passed.");
            }
        }

        result.addTechnicalDetails(StringUtils.substituteParameters("Email address: %1", emailAddress));
        result.addTechnicalDetails(getProfileDescription(outgoingProfile, incomingProfile));
        result.addTechnicalDetails(getSystemInformation());

        String errorConnection = "";
        if (!result.getErrorMessages().isEmpty()) {
            String domain = emailAddress.substring(emailAddress.indexOf("@") + 1);
            String message = String.join("\n", result.getErrorMessages().toArray(new String[0]));
            String technicalDetails = String.join("\n\n", result.getTechnicalDetails().toArray(new String[0]));

            List<String> recommendationList = new ArrayList<>();
            if (authError)
                recommendationList.add("Check the correctness of login and password, as well as the chosen authentication method.");
            if (needToCheckOutgoingProfile)
                recommendationList.add("Check the settings for connecting to the outgoing mail server.");
            if (needToCheckIncomingProfile)
                recommendationList.add("Check the settings for connecting to the incoming mail server.");
            if (needToCheckOutgoingProfile || needToCheckIncomingProfile)
                recommendationList.add("Contact the local network administrator.");

            String recommendations = String.join("\n", recommendationList.toArray(new String[0]));
            errorConnection = String.format("%s\n|\n|%s\n|\nContact the administrator of the \"%s\" mail server.\n|\n============================\n|\nInformation for technical support:\n|\n%s",
                    message, recommendations, domain, technicalDetails);
        }

        if (!errorConnection.isEmpty()) {
            LOGGER.warn(errorConnection); // TODO: Запись в журнал регистрации
        }

        return result;
    }

    // Alias: ОписаниеНастроек
    private String getProfileDescription(InternetMailProfile outgoingProfile, InternetMailProfile incomingProfile) {
        StringBuilder description = new StringBuilder();
        if (outgoingProfile != null) {
            description.append("Outgoing profile: ")
                    .append(outgoingProfile.getSmtpServerAddress()).append(":")
                    .append(outgoingProfile.getSmtpPort()).append(", username: ").append(outgoingProfile.getSmtpUsername());

            if (outgoingProfile.isUseSSLForSmtp()) {
                description.append(" (SSL)");
            }
        }

        if (incomingProfile != null) {
            description.append("\nIncoming profile: ")
                    .append(incomingProfile.getImapServerAddress()).append(":")
                    .append(incomingProfile.getImapPort()).append(", username: ").append(incomingProfile.getImapUsername());

            if (incomingProfile.isUseSSLForImap()) {
                description.append(" (SSL)");
            }
        }

        return description.toString();
    }

    // Alias: ИнформацияОПрограмме
    private String getSystemInformation() {
        return "Operating system: " +
                System.getProperty("os.name") +
                " (" + System.getProperty("os.version") + ")" +
                "Platform: " +
                System.getProperty("java.vendor") +
                " (" + System.getProperty("java.version") + ")" +
                "Configuration: " +
                System.getProperty("java.runtime.name") +
                " (" + System.getProperty("java.runtime.version") + ")";

        // TODO: Добавить поддержку расширений и выводить их сюда
    }
}
