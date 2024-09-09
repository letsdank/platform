package net.letsdank.platform.utils.mail;

import net.letsdank.platform.module.email.model.EmailMessageAddress;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MailUtilsTest {
    @Test
    void parseAddressList() {
        String addresses = "email1 <email3@mail.net>, test <testemail@mail.net>";

        List<EmailMessageAddress> result = MailUtils.parseAddresses(addresses);

        System.out.println(result);

        EmailMessageAddress address1 = new EmailMessageAddress("email3@mail.net", "email1");
        EmailMessageAddress address2 = new EmailMessageAddress("testemail@mail.net", "test");

        assertEquals(2, result.size());
        assertEquals(address1, result.get(0));
        assertEquals(address2, result.get(1));
    }

}
