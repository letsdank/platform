package net.letsdank.platform.repository.finance;

import net.letsdank.platform.entity.finance.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
