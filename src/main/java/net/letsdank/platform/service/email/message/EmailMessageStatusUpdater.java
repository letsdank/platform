package net.letsdank.platform.service.email.message;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailMessageStatusUpdater {
    private ApplicationEventPublisher eventPublisher;

    public void getEmailMessagesStatus() {
        List<EmailMessageStatusIdentifier> identifiers = new ArrayList<>();
        eventPublisher.publishEvent(new EmailMessageStatusBeforeUpdateEvent(this, identifiers));

        if (identifiers.isEmpty()) {
            return;
        }

        // TODO: implement
    }
}
