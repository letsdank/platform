package net.letsdank.platform.utils.string;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public class MessageService {
    private static final MessageSource messageSource;

    static {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames("common/messages");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        messageSource = resourceBundleMessageSource;
    }

    public static String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
