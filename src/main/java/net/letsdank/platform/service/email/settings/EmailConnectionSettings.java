package net.letsdank.platform.service.email.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.service.auth.ServiceAuthenticationSettingsObject;
import net.letsdank.platform.utils.platform.mail.InternetMailProfile;

@AllArgsConstructor
@Getter
@Setter
public class EmailConnectionSettings {
    private InternetMailProfile profile;
    private String mailServer;
    private ServiceAuthenticationSettingsObject authorizationSettings; // TODO: Implement authorization settings
}
