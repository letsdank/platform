package net.letsdank.platform.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceAuthenticationSettings {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 200)
    private String ownerName;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(columnDefinition = "TEXT")
    private String authorizationUri;

    @Column(columnDefinition = "TEXT")
    private String grantKeyUri;

    @Column(length = 200)
    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String appIdentifier;

    @Column(columnDefinition = "TEXT")
    private String redirectUri;

    @Column(columnDefinition = "boolean default false")
    private boolean usePkce;

    @Column(columnDefinition = "TEXT")
    private String scopes;

    @Column(columnDefinition = "boolean default false")
    private boolean useClientSecret;

    @Column(columnDefinition = "TEXT")
    private String additionalAuthorizationParameters;

    @Column(columnDefinition = "TEXT")
    private String additionalGrantParameters;

    @Column(columnDefinition = "TEXT")
    private String redirectUriWebClient; // TODO: Я думаю, что это не нужно.

    @Column(columnDefinition = "TEXT")
    private String registerDeviceUri;
}
