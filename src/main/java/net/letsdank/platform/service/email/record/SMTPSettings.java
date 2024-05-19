package net.letsdank.platform.service.email.record;

public record SMTPSettings(String host, int port, boolean tls) {
}
