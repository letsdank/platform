package net.letsdank.platform.service.email.record;

public record IMAPSettings(String host, int port, boolean ssl) {
}
