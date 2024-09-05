package net.letsdank.platform.service.email.message;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class EmailMessageStatusBeforeUpdateEvent extends ApplicationEvent {
    private final List<EmailMessageStatusIdentifier> identifiers;

    public EmailMessageStatusBeforeUpdateEvent(Object source, List<EmailMessageStatusIdentifier> identifiers) {
        super(source);
        this.identifiers = identifiers;
    }

    public void addIdentifier(EmailMessageStatusIdentifier identifier) {
        this.identifiers.add(identifier);
    }
}
