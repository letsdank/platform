package net.letsdank.platform.repository.common;

import net.letsdank.platform.entity.email.EmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAccountRepository extends JpaRepository<EmailAccount, Long> {

}
