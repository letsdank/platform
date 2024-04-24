package net.letsdank.platform.repository.finance;

import net.letsdank.platform.entity.finance.BankClassifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankClassifierRepository extends JpaRepository<BankClassifier, Long> {
    @Query("SELECT bc FROM BankClassifier bc WHERE bc.deleted = false AND bc.isGroup = true order by bc.id")
    List<BankClassifier> findAllGroups();

    @Query("SELECT bc FROM BankClassifier bc WHERE bc.deleted = false AND bc.isGroup = true AND bc.name ilike %:query% order by bc.name")
    List<BankClassifier> findGroupSuggestions(String query);

    @Query("SELECT bc FROM BankClassifier bc WHERE bc.deleted = false AND bc.isGroup = false AND bc.name ilike %:query% order by bc.name")
    List<BankClassifier> findItemSuggestions(String query);
}
