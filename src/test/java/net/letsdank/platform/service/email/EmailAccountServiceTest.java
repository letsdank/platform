package net.letsdank.platform.service.email;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailAccountServiceTest {

    @Test
    void determineMailServersForDomain() {
        String domain = "mail.ru";
        String mxDomain = "mxs.mail.ru";

        List<String> result = EmailAccountService.determineMailServersForDomain(domain);

        assertTrue(result.contains(mxDomain));

        domain = "gmail.com";
        mxDomain = "gmail-smtp-in.l.google.com";

        result = EmailAccountService.determineMailServersForDomain(domain);

        assertTrue(result.contains(mxDomain));
        assertFalse(result.contains("domain-that-will-not-exists.com"));
    }
}