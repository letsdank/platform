package net.letsdank.platform.service.finance;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.finance.Bank;
import net.letsdank.platform.model.common.ErrorMessage;
import net.letsdank.platform.repository.finance.BankRepository;
import net.letsdank.platform.utils.MessageService;
import net.letsdank.platform.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BankService {
    private final BankRepository bankRepository;

    // Disables bank support
    // Alias: СнятьСПоддержки
    public void disableSupport(Bank bank) {
        // TODO: Цифры
        bank.setManualModification(2);
        bankRepository.save(bank);
    }

    public List<ErrorMessage> save(Bank bank) {
        List<ErrorMessage> result = new ArrayList<>();

        // TODO: Сделать классификатор предзаполненных значений в коде
        if (Objects.equals(bank.getCountry().getName(), "РОССИЯ")) {
            // Если это не группа, проверяем значения, иначе пропускаем
            if (!bank.isGroup()) {
                String message = null, fieldName = null;
                if (bank.getCorrAccount() != null && bank.getCorrAccount().length() != 20) {
                    message = MessageService.getMessage("common.bank.edit.corr-account.length");
                    fieldName = "corrAccount";
                    result.add(new ErrorMessage(message, fieldName));
                }

                if (bank.getCorrAccount() != null && !StringUtils.isDigitsOnly(bank.getCorrAccount().trim())) {
                    message = MessageService.getMessage("common.bank.edit.corr-account.numbers");
                    fieldName = "corrAccount";
                    result.add(new ErrorMessage(message, fieldName));
                }

                if (bank.getCode().length() != 9) {
                    message = MessageService.getMessage("common.bank.edit.code.length");
                    fieldName = "code";
                    result.add(new ErrorMessage(message, fieldName));
                }

                if (!StringUtils.isDigitsOnly(bank.getCode().trim())) {
                    message = MessageService.getMessage("common.bank.edit.code.numbers");
                    fieldName = "code";
                    result.add(new ErrorMessage(message, fieldName));
                }
            }
        } else {
            // TODO: Непонятно, что это за функция (изучить)
            // ЗаполнениеОбъектовУНФ.УдалитьПроверяемыйРеквизит(ПроверяемыеРеквизиты, "Код");
            bank.setCode(null);
        }

        String message = isSwiftValid(bank.getSwiftBic());
        if (message != null) {
            result.add(new ErrorMessage(message, "swiftBic"));
        }

        if (result.isEmpty()) {
            bankRepository.save(bank);
        }

        return result;
    }

    /**
     * Checks the correctness of the bank's SWIFT number.
     *
     * @param swiftBic the SWIFT number of the bank to be checked
     * @return The result of the SWIFT check, if <code>null</code>, then
     * the SWIFT is correct
     */
    // Alias: SWIFTКорректен
    private String isSwiftValid(String swiftBic) {
        if (swiftBic.isEmpty()) {
            return null;
        }

        boolean correct = false;
        if (!BankDataRules.isSwiftLengthValid(swiftBic)) {
            return MessageService.getMessage("common.bank.edit.swift.length");
        } else if (!BankDataRules.isSwiftCharactersValid(swiftBic)) {
            return MessageService.getMessage("common.bank.edit.swift.letters");
        }

        return null;
    }
}
