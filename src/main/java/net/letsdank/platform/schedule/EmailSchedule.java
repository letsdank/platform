package net.letsdank.platform.schedule;

import lombok.AllArgsConstructor;
import net.letsdank.platform.service.email.message.EmailMessageStatusUpdater;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailSchedule {
    private final EmailMessageStatusUpdater emailMessageStatusUpdater;

    @Scheduled(fixedRate = 1800 * 1000)
    public void getEmailMessagesStatus() {
        emailMessageStatusUpdater.getEmailMessagesStatus();
    }
}
