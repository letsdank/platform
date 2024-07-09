package net.letsdank.platform.service.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Test
    void encodePunycode() {
        String domain = "example.com";
        String punycode = "example.com";
        assertEquals(punycode, EmailService.convertToPunycode(domain));

        domain = " café.fr";
        punycode = "xn--caf-dma.fr";
        assertEquals(punycode, EmailService.convertToPunycode(domain));

        domain = "中国新闻网.cn";
        punycode = "xn--fiqs8s.cn";
        // TODO: Почему-то не принимает иероглифы
        // Надо выяснить, принимает ли их 1С, и предпринять решение
        // assertEquals(punycode, EmailService.convertToPunycode(domain));

        domain = "рф.рф";
        punycode = "xn--p1ai.xn--p1ai";
        assertEquals(punycode, EmailService.convertToPunycode(domain));
    }
}