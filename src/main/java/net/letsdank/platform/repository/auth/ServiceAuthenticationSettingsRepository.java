package net.letsdank.platform.repository.auth;

import net.letsdank.platform.entity.auth.ServiceAuthenticationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceAuthenticationSettingsRepository extends JpaRepository<ServiceAuthenticationSettings, Long> {
    @Query("SELECT s.id, s.authorizationUri, s.grantKeyUri, s.registerDeviceUri, s.redirectUri, s.redirectUriWebClient, " +
            "s.usePkce, s.appIdentifier, s.scopes, s.useClientSecret, s.additionalAuthorizationParameters, " +
            "s.additionalGrantParameters, s.serviceName, s.ownerName FROM ServiceAuthenticationSettings s " +
            "WHERE s.serviceName = ?1 " +
            "AND s.ownerName = ?2 " +
            "AND s.deleted = false")
    ServiceAuthenticationSettings findByServiceNameAndOwnerName(String serviceName, String ownerName);
}
