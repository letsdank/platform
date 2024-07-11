package net.letsdank.platform.service.email.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.mail.InternetMailProfile;

@AllArgsConstructor
@Getter
@Setter
public class EmailConnectionSettings {
    private InternetMailProfile profile;
    private String mailServer;
    private Object authorizationSettings; // TODO: Implement authorization settings
}
