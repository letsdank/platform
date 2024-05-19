package net.letsdank.platform.service.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.email.EmailAccount;
import net.letsdank.platform.repository.common.EmailAccountRepository;
import net.letsdank.platform.service.email.EmailService;
import net.letsdank.platform.service.email.record.IMAPSettings;
import net.letsdank.platform.service.email.record.SMTPSettings;
import net.letsdank.platform.utils.string.StringUtils;
import net.letsdank.platform.utils.url.URLInfo;
import net.letsdank.platform.utils.url.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAccountService.class);

    private final EmailAccountRepository emailAccountRepository;

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public void obtainConnectionSettingsByEmailAddress(String address) {
        obtainConnectionSettingsByEmailAddress(address, "");
    }

    // Alias: НастройкиПодключенияПоАдресуЭлектроннойПочты
    public void obtainConnectionSettingsByEmailAddress(String address, String password) {
        URLInfo urlInfo = URLUtils.getURLInfo(address);
        String mailDomain = urlInfo.getHost();

        // Struct result = null;
        String mailServerName = "";
        Map<String, Object> mailServerSettings = getMailServerSettings();
        if (mailServerSettings != null) {
            Object foundSettings = mailServerSettings.get(mailDomain);
            if (foundSettings instanceof String) {
                mailServerName = (String) foundSettings;
                foundSettings = mailServerSettings.get(mailServerName);
            }

            if (foundSettings == null) {
                // Если по основному домену ничего не найдено, то определяем имена почтовых серверов
                // домена, и по ним пытаемся определить настройки
                List<String> serverNames = getMailServerDomainNames(mailDomain);
            }
        }
    }

    private List<String> getMailServerDomainNames(String domain) {
        List<String> result = new ArrayList<>();

        List<String> dnsServers = EmailService.getDnsServers();
        dnsServers.add(0, ""); // Сервер по умолчанию

        List<String> commands = new ArrayList<>();
        for (String server : dnsServers) {
            String commandTemplate = "nslookup -type=mx %1 %2";
            String command = StringUtils.substituteParameters(commandTemplate, domain, server);
            commands.add(command);

            // Запустить команду nslookup
        }
    }

    public Map<String, Object> getMailServerSettings() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource(EmailService.getServerSettingsPath());

            return mapper.readValue(resource.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmailAccount> getAvailableAccounts() {
        return null; // TODO:
    }

    // Alias: ВариантыНастройкиПодключенияКСерверуIMAP
    public List<IMAPSettings> getIMAPSettings(String address) {
        int position = address.indexOf("@");
        String serverName = address.substring(position + 1);

        List<IMAPSettings> result = new ArrayList<>();

        if (serverName.equals("icloud.com")) {
            result.add(new IMAPSettings("imap.mail.me.com", 993, true));
            return result;
        }

        if (serverName.equals("outlook.com")) {
            result.add(new IMAPSettings("outlook.office365.com", 993, true));
            return result;
        }

        result.add(new IMAPSettings("imap." + serverName, 993, true));
        result.add(new IMAPSettings("mail." + serverName, 993, true));
        result.add(new IMAPSettings(serverName, 993, true));
        result.add(new IMAPSettings("imap." + serverName, 993, false));
        result.add(new IMAPSettings("mail." + serverName, 993, false));
        result.add(new IMAPSettings(serverName, 993, false));

        return result;
    }

    // Alias: ВариантыНастройкиПодключенияКСерверуSMTP
    public List<SMTPSettings> getSMTPSettings(String address) {
        int position = address.indexOf("@");
        String serverName = address.substring(position + 1);

        List<SMTPSettings> result = new ArrayList<>();

        if (serverName.equals("icloud.com")) {
            result.add(new SMTPSettings("smtp.mail.me.com", 587, false));
            return result;
        }

        if (serverName.equals("outlook.com")) {
            result.add(new SMTPSettings("smtp-mail.outlook.com", 587, false));
            return result;
        }

        result.add(new SMTPSettings("smtp." + serverName, 465, true));
        result.add(new SMTPSettings("mail." + serverName, 465, true));
        result.add(new SMTPSettings(serverName, 465, true));
        result.add(new SMTPSettings("smtp." + serverName, 587, false));
        result.add(new SMTPSettings("mail." + serverName, 587, false));
        result.add(new SMTPSettings(serverName, 587, false));
        result.add(new SMTPSettings("smtp." + serverName, 25, false));
        result.add(new SMTPSettings("mail." + serverName, 25, false));
        result.add(new SMTPSettings(serverName, 25, false));

        return result;
    }

    // TODO: Здесь должен быть какой-то профиль
    public String checkMailOutgoingConnection(EmailAccount account, String address) {
        String subject = "Тестовое сообщение 1С:Предприятие";
        String body = "Это сообщение отправлено подсистемой электронной почты 1С:Предприятие";
        String fromName = "1C:Предприятие";

        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom(address, fromName);
            messageHelper.setTo(new InternetAddress(address, fromName));
            messageHelper.setSubject(subject);
            messageHelper.setText(body);
        };

        String errorMessage = "";
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.send(mailMessage);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        LOGGER.info(StringUtils.substituteParameters("%1:%2%3 (%4)\n%5",
                account.getAddress(),
                String.valueOf(account.getPortOutgoing()),
                account.isUseSslForSend() ? "/SSL" : "",
                account.getUsername(),
                errorMessage.isEmpty() ? "OK" : errorMessage));

        return errorMessage;
    }
}
