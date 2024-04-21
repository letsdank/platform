package net.letsdank.platform.service.finance;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.finance.Currency;
import net.letsdank.platform.entity.finance.ExchangeRateSettingMethod;
import net.letsdank.platform.model.common.ErrorMessage;
import net.letsdank.platform.repository.finance.CurrencyRepository;
import net.letsdank.platform.utils.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateService exchangeRateService;

    public List<ErrorMessage> save(Currency currency) {
        List<ErrorMessage> result = new ArrayList<>();
        boolean independentError = false;

        if (currency.getExchangeRateSetting() == ExchangeRateSettingMethod.FORMULA) {
            List<String> dependentCurrencies = currencyRepository.getDependentCurrencies();

            for (String dependentCurrency : dependentCurrencies) {
                if (currency.getExchangeRateCalc().contains(dependentCurrency)) {
                    independentError = true;
                    break;
                }
            }
        }

        if (currency.getBaseCurrency().getBaseCurrency() != null) {
            independentError = true;
        }

        if (independentError) {
            result.add(new ErrorMessage(MessageService.getMessage("common.currency.edit.independent"), null));
        }

        if (currency.getExchangeRateSetting() != ExchangeRateSettingMethod.MARKUP) {
            // TODO: Убираем валидацию baseCurrency и markup
        }

        if (currency.getExchangeRateSetting() != ExchangeRateSettingMethod.FORMULA) {
            // TODO: Убираем валидацию exchangeRateCalc
        }

        if (currency.getId() != null &&
                currency.getExchangeRateSetting() == ExchangeRateSettingMethod.MARKUP &&
                !exchangeRateService.getDependentCurrencies(currency).isEmpty()
        ) {
            result.add(new ErrorMessage(MessageService.getMessage("common.currency.edit.subordinate"), null));
        }

        if (result.isEmpty()) {
            currencyRepository.save(currency);
        }

        return result;
    }
}
