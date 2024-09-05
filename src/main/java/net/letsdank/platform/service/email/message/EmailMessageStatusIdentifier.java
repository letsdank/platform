package net.letsdank.platform.service.email.message;

import net.letsdank.platform.entity.email.EmailAccount;

public record EmailMessageStatusIdentifier(EmailAccount sender, String messageId, String addressTo) {
}
