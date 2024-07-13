package net.letsdank.platform.service.email.mapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.letsdank.platform.service.email.server.*;
import net.letsdank.platform.utils.mail.InternetMailProtocol;

import java.io.IOException;
import java.util.Map;

public class EmailMailServerSettingsDeserializer extends StdDeserializer<EmailMailServerSettings> {
    public EmailMailServerSettingsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public EmailMailServerSettings deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        EmailMailServerSettings settings = new EmailMailServerSettings();

        for (Map.Entry<String, JsonNode> entry : node.properties()) {
            JsonNode value = entry.getValue();
            if (value.isTextual()) {
                settings.addAlias(entry.getKey(), value.asText());
                continue;
            }

            settings.addServer(entry.getKey(), deserializeEmailServer(value));
        }

        return settings;
    }

    private EmailServerSettings deserializeEmailServer(JsonNode node) {
        EmailServerSettings server = new EmailServerSettings();
        server.setWebSite(getString(node, "webSite"));

        for (JsonNode jsonService : node.findValues("services")) {
            EmailServerAddress address = new EmailServerAddress();
            address.setProtocol(getEnum(node, InternetMailProtocol.class, "protocol"));
            address.setHost(getString(jsonService, "host"));
            address.setPort(getInt(jsonService, "port"));
            address.setEncryption(getEnum(node, EmailServerAddressEncryption.class, "encryption"));
            address.setLoginFormat(getString(jsonService, "loginFormat"));
            address.setSmtpAuthentication(getString(jsonService, "smtpAuthentication"));
            address.setMustBeEnabled(getBoolean(node, "mustBeEnabled"));

            server.getServices().add(address);
        }

        JsonNode oauthNode = node.get("oauth");
        ObjectMapper mapper = new ObjectMapper();
        if (oauthNode != null) {
            EmailServerOAuthSettings oauthSettings = new EmailServerOAuthSettings();
            oauthSettings.setAuthorizationURI(getString(oauthNode, "authorizationUri"));
            oauthSettings.setTokenExchangeURI(getString(oauthNode, "tokenExchangeUri"));
            oauthSettings.setMailScope(oauthNode.findValuesAsText("mailScope"));
            oauthSettings.setUsePKCE(getBoolean(node, "usePkce"));
            oauthSettings.setUseClientSecret(getBoolean(oauthNode, "useClientSecret"));
            oauthSettings.setAuthorizationParameters(
                    mapper.convertValue(oauthNode.get("authorizationParameters"), Map.class));
            oauthSettings.setTokenExchangeParameters(getString(oauthNode, "tokenExchangeParameters"));
            oauthSettings.setRedirectURIDescription(getInternationalString(oauthNode, "redirectUriDescription"));
            oauthSettings.setRedirectURICaption(getInternationalString(oauthNode, "redirectUriCaption"));
            oauthSettings.setDefaultRedirectURI(getString(oauthNode, "defaultRedirectUri"));
            oauthSettings.setClientIDDescription(getInternationalString(oauthNode, "clientIdDescription"));
            oauthSettings.setClientIDCaption(getInternationalString(oauthNode, "clientIdCaption"));
            oauthSettings.setClientSecretDescription(getInternationalString(oauthNode, "clientSecretDescription"));
            oauthSettings.setClientSecretCaption(getInternationalString(oauthNode, "clientSecretCaption"));
            oauthSettings.setAdditionalDescription(getInternationalString(oauthNode, "additionalDescription"));

            server.setOauthSettings(oauthSettings);
        }

        return server;
    }

    private InternationalString getInternationalString(JsonNode node, String key) {
        InternationalString result = new InternationalString();
        for (Map.Entry<String, JsonNode> entry : node.get(key).properties()) {
            result.addValue(entry.getKey(), entry.getValue().toString());
        }

        return result;
    }

    private boolean getBoolean(JsonNode node, String key) {
        return node.has(key) && node.get(key).asBoolean();
    }

    private int getInt(JsonNode node, String key) {
        return node.has(key)? node.get(key).asInt() : 0;
    }

    private String getString(JsonNode node, String key) {
        return node.has(key)? node.get(key).asText() : null;
    }

    private <E extends Enum<E>> E getEnum(JsonNode node, Class<E> enumClass, String key) {
        return node.has(key) ? Enum.valueOf(enumClass, node.get(key).asText()) : null;
    }
}
