package net.letsdank.platform.repository.finance;

import net.letsdank.platform.entity.finance.Currency;
import net.letsdank.platform.entity.finance.ExchangeRateSettingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.EnumSet;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    default List<String> getDependentCurrencies() {
        return _getDependentCurrencies(EnumSet.of(ExchangeRateSettingMethod.MARKUP, ExchangeRateSettingMethod.FORMULA));
    }

    @Query("SELECT c.name from Currency c where c.exchangeRateSetting IN :list")
    List<String> _getDependentCurrencies(EnumSet<ExchangeRateSettingMethod> list);

    @Query("SELECT c FROM Currency c " +
            "WHERE c.baseCurrency = :baseCurrency " +
            "OR c.exchangeRateCalc ilike %:baseCurrency%")
    List<Currency> getDependentBaseCurrencies(Currency baseCurrency);
}
