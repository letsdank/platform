package net.letsdank.platform.utils.event;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventPublisherHolder {
    private final ApplicationEventPublisher publisher;

    private static EventPublisherHolder instance;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static void publishEvent(ApplicationEvent event) {
        instance.publisher.publishEvent(event);
    }
}
