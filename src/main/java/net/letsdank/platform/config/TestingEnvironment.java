package net.letsdank.platform.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

// Заполняем то, что нужно отладить
@Component
@AllArgsConstructor
public class TestingEnvironment {

    @PostConstruct
    public void init() {
    }
}
