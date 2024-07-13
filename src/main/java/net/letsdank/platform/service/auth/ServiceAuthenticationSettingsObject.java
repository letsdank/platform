package net.letsdank.platform.service.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.letsdank.platform.entity.auth.ServiceAuthenticationSettings;

@NoArgsConstructor
@Getter
@Setter
public class ServiceAuthenticationSettingsObject {
    private Long id;                                    // Alias: Ссылка
    private String serviceName;                         // Alias: ИмяИнтернетСервиса
    private String ownerName;                           // Alias: ВладелецДанных
    private String authorizationUri;                    // Alias: АдресАвторизации
    private String registerDeviceUri;                   // Alias: АдресРегистрацииУстройства
    private String grantKeyUri;                         // Alias: АдресПолученияКлюча
    private String redirectUri;                         // Alias: АдресПеренаправления
    private String redirectUriWebClient;                // Alias: АдресПеренаправленияВебКлиент
    private String scopes;                              // Alias: ЗапрашиваемыеРазрешения
    private boolean usePkce;                            // Alias: ИспользоватьКлючПроверкиПодлинностиPKCE
    private String appIdentifier;                       // Alias: ИдентификаторПриложения
    private boolean useClientSecret;                    // Alias: ИспользоватьПарольПриложения
    private String clientSecret;                        // Alias: ПарольПриложения
    private String additionalAuthorizationParameters;   // Alias: ДополнительныеПараметрыАвторизации
    private String additionalGrantParameters;           // Alias: ДополнительныеПараметрыПолученияТокена
    private String redirectUriDescription;              // Alias: ПояснениеПоАдресуПеренаправления
    private String clientIdDescription;                 // Alias: ПояснениеПоИдентификаторуПриложения
    private String clientSecretDescription;             // Alias: ПояснениеПоПаролюПриложения
    private String additionalDescription;               // Alias: ДополнительноеПояснение
    private String redirectUriCaption;                  // Alias: ПсевдонимАдресаПеренаправления
    private String clientIdCaption;                     // Alias: ПсевдонимИдентификатораПриложения
    private String clientSecretCaption;                 // Alias: ПсевдонимПароляПриложения

    public void fillData(ServiceAuthenticationSettings settings) {
        setId(settings.getId());
        setServiceName(settings.getServiceName());
        setOwnerName(settings.getOwnerName());
        setAuthorizationUri(settings.getAuthorizationUri());
        setRegisterDeviceUri(settings.getRegisterDeviceUri());
        setGrantKeyUri(settings.getGrantKeyUri());
        setRedirectUri(settings.getRedirectUri());
        setRedirectUriWebClient(settings.getRedirectUriWebClient());
        setScopes(settings.getScopes());
        setUsePkce(settings.isUsePkce());
        setAppIdentifier(settings.getAppIdentifier());
        setUseClientSecret(settings.isUseClientSecret());
        setAdditionalAuthorizationParameters(settings.getAdditionalAuthorizationParameters());
        setAdditionalGrantParameters(settings.getAdditionalGrantParameters());
    }
}
