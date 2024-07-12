package net.letsdank.platform.service.email.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class EmailServer {
    private List<EmailServerAddress> services = new ArrayList<>();
    private String webSite;
    private EmailServerOAuthSettings oauthSettings;
}
