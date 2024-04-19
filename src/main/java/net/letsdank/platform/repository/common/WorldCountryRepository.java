package net.letsdank.platform.repository.common;

import net.letsdank.platform.entity.common.WorldCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorldCountryRepository extends JpaRepository<WorldCountry, Long> {
    @Query("SELECT c FROM WorldCountry c " +
            "WHERE (c.code = :code " +
            "OR c.name = :name " +
            "OR c.codeAlpha2 = :alpha2Code " +
            "OR c.codeAlpha3 = :alpha3Code " +
            "OR c.fullName = :fullName) " +
            "AND c.id != :id ORDER BY c.id limit 10")
    List<WorldCountry> findDuplicates(String code, String name, String alpha2Code, String alpha3Code, String fullName, Long id);
}
