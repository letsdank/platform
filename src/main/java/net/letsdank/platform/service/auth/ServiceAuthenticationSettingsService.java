package net.letsdank.platform.service.auth;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.auth.ServiceAuthenticationSettings;
import net.letsdank.platform.repository.auth.ServiceAuthenticationSettingsRepository;
import net.letsdank.platform.service.common.storage.SafeStorage;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServiceAuthenticationSettingsService {
    private final ServiceAuthenticationSettingsRepository repository;
    private final SafeStorage storage;

    public ServiceAuthenticationSettingsObject getSettings(String serviceName, String ownerName) {
        ServiceAuthenticationSettingsObject result = new ServiceAuthenticationSettingsObject();
        ServiceAuthenticationSettings settings = repository.findByServiceNameAndOwnerName(serviceName, ownerName);

        if (settings != null) {
            result.fillData(settings);
            result.setClientSecret(storage.getValue(
                    ServiceAuthenticationSettings.class, settings.getId(), "clientSecret"));
        }

        return result;
    }
}
