package net.letsdank.platform.controller.entity.email;

import lombok.AllArgsConstructor;
import net.letsdank.platform.service.email.EmailAccountService;
import net.letsdank.platform.service.email.settings.EmailConnectionSettings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entity/email-account/helper")
@AllArgsConstructor
public class EmailAccountHelperController {
    private final EmailAccountService emailAccountService;

    @GetMapping("/mail-server-settings")
    public EmailConnectionSettings getMailServerSettings(@RequestParam String emailAddress,
                                                         @RequestParam String passwordOutgoing) {
        EmailConnectionSettings result = emailAccountService.getEmailSettingsByEmail(emailAddress, passwordOutgoing);
        return result;
    }
}
