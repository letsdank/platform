package net.letsdank.platform.repository.common;

import net.letsdank.platform.entity.common.WorldCountry;
import net.letsdank.platform.entity.finance.BankClassifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorldCountryRepository extends JpaRepository<WorldCountry, Long> {
    @Query("SELECT c FROM  WorldCountry c " +
            "WHERE c.deleted = false AND c.name ilike %:query% " +
            "ORDER by c.name ASC")
    List<WorldCountry> findSuggestions(String query);

    @Query("SELECT c FROM WorldCountry c " +
            "WHERE c.deleted = false ORDER by c.name ASC")
    Page<WorldCountry> findAllPaged(Pageable pageable);

    @Query("SELECT c FROM WorldCountry c " +
            "WHERE (c.code = :code " +
            "OR c.name = :name " +
            "OR c.codeAlpha2 = :alpha2Code " +
            "OR c.codeAlpha3 = :alpha3Code " +
            "OR c.fullName = :fullName) " +
            "AND c.id != :id and c.deleted = false ORDER BY c.id limit 10")
    List<WorldCountry> findDuplicates(String code, String name, String alpha2Code, String alpha3Code, String fullName, Long id);

    boolean existsByCodeAndDeletedFalse(String code);
    Optional<WorldCountry> findByCodeAndDeletedFalse(String code);

    @Query("SELECT c.code FROM WorldCountry c " +
            "WHERE c.predefined = true")
    List<String> findPredefinedCodes();
}
