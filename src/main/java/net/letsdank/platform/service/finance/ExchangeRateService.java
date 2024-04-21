package net.letsdank.platform.service.finance;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.finance.Currency;
import net.letsdank.platform.repository.finance.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Alias: РаботаСКурсамиВалют
@Service
@AllArgsConstructor
public class ExchangeRateService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> getDependentCurrencies(Currency baseCurrency) {
        return currencyRepository.getDependentBaseCurrencies(baseCurrency);
    }
}
