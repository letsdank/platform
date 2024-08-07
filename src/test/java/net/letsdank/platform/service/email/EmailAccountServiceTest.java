package net.letsdank.platform.service.email;

import net.letsdank.platform.service.auth.ServiceAuthenticationSettingsObject;
import net.letsdank.platform.service.auth.ServiceAuthenticationSettingsService;
import net.letsdank.platform.service.email.settings.EmailSettingsResult;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EmailAccountServiceTest {
    @InjectMocks
    private EmailAccountService emailAccountService;

    @Mock
    private ServiceAuthenticationSettingsService serviceAuthenticationSettingsService;

    @Test
    void determineMailServersForDomain() {
        MockitoAnnotations.openMocks(this);

        String domain = "mail.ru";
        String mxDomain = "mxs.mail.ru";

        List<String> result = emailAccountService.determineMailServersForDomain(domain);
        System.out.println("Found mail exchange servers for domain: \n" +
                String.join(", ", result.toArray(new String[0])));
        assertTrue(result.contains(mxDomain));

        domain = "gmail.com";
        mxDomain = "gmail-smtp-in.l.google.com";

        result = emailAccountService.determineMailServersForDomain(domain);

        System.out.println("Found mail exchange servers for domain: \n" +
                String.join(", ", result.toArray(new String[0])));
        assertTrue(result.contains(mxDomain));
        assertFalse(result.contains("domain-that-will-not-exists.com"));
    }

    @Test
    void testDetermineMailServersForDomain() {
        MockitoAnnotations.openMocks(this);
    }
}