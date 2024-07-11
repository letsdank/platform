package net.letsdank.platform.service.email.server;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class EmailMailServerSettings {
    private final Map<String, Object> servers = new HashMap<>();

    public Set<String> keySet() {
        return servers.keySet();
    }

    public boolean isAlias(String key) {
        return servers.get(key) instanceof String;
    }

    public boolean isServer(String key) {
        return servers.get(key) instanceof EmailServer;
    }

    public String getAlias(String key) {
        if (isAlias(key)) {
            return (String) servers.get(key);
        }
        return null;
    }

    public EmailServer getServer(String key) {
        if (isServer(key)) {
            return (EmailServer) servers.get(key);
        }
        return null;
    }
}
