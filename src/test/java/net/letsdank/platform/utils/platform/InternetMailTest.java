package net.letsdank.platform.utils.platform;

import net.letsdank.platform.utils.platform.mail.InternetMail;
import net.letsdank.platform.utils.platform.mail.InternetMailMessage;
import net.letsdank.platform.utils.platform.mail.InternetMailProfile;
import org.junit.jupiter.api.Test;

public class InternetMailTest {
    @Test
    public void sendMail() {
        InternetMail mail = new InternetMail();
        InternetMailProfile profile = new InternetMailProfile();

        profile.setSmtpServerAddress("smtp.freesmtpservers.com");
        profile.setSmtpPort(25);

        InternetMailMessage message = new InternetMailMessage();
        message.setSubject("Test");
        message.addBody("Test", "type=text/plain; charset=UTF-8");
        message.addTo("test_email@mail.ru", null);

        message.addCc("test_cc1@freesmtpservers.com", null);
        message.addCc("test_cc2@freesmtpservers.com", null);
        message.addCc("test_cc3@freesmtpservers.com", null);
        message.addBcc("test_bcc@freesmtpservers.com", null);
        message.addReplyTo("test_reply@freesmtpservers.com", null);

        message.setFrom("test@freesmtpservers.com", null);

        mail.connect(profile);
        mail.sendMessage(message);
    }
}
