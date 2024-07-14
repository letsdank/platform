package net.letsdank.platform.service.email;

import net.letsdank.platform.module.email.EmailMessageImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailMessageImplTest {

    @Test
    void encodePunycode() {
        String domain = "example.com";
        String punycode = "example.com";
        assertEquals(punycode, EmailMessageImpl.convertToPunycode(domain));
        assertEquals(domain, EmailMessageImpl.punycodeToString(punycode));

        domain = " café.fr";
        punycode = "xn--caf-dma.fr";
        assertEquals(punycode, EmailMessageImpl.convertToPunycode(domain));
        // assertEquals(domain, EmailService.convertToPunycode(punycode));

        domain = "中国新闻网.cn";
        punycode = "xn--fiqs8s.cn";
        // TODO: Почему-то не принимает иероглифы
        // Надо выяснить, принимает ли их 1С, и предпринять решение
        // assertEquals(punycode, EmailService.convertToPunycode(domain));

        domain = "рф.рф";
        punycode = "xn--p1ai.xn--p1ai";
        assertEquals(punycode, EmailMessageImpl.convertToPunycode(domain));
        assertEquals(domain, EmailMessageImpl.punycodeToString(punycode));

        domain = "госуслуги.рф";
        punycode = "xn--c1aapkosapc.xn--p1ai";
        assertEquals(punycode, EmailMessageImpl.convertToPunycode(domain));
        assertEquals(domain, EmailMessageImpl.punycodeToString(punycode));
    }
}