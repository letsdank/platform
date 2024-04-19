package net.letsdank.platform.service.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.letsdank.platform.entity.common.WorldCountry;
import net.letsdank.platform.repository.common.WorldCountryRepository;
import net.letsdank.platform.utils.MessageService;
import net.letsdank.platform.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class WorldCountryService {
    private final WorldCountryRepository worldCountryRepository;

    // Alias:
    // ПередЗаписью
    // ОбработкаПроверкиЗаполнения
    public List<ErrorMessage> save(WorldCountry country) {
        if (country.getId() != null && country.isPredefined()) {
            checkPredefinedValueEditing(country);
        }

        return checkEntityUniqueness(country);
    }

    // Alias: ПроверитьИзменениеПредопределенногоЭлемента
    private void checkPredefinedValueEditing(WorldCountry country) {
        WorldCountry previous = worldCountryRepository.getReferenceById(country.getId());
        if (!Objects.equals(previous.getName(), country.getName())) {
            String message = MessageService.getMessage("common.world-country.edit.not-allowed.name");
            throw new IllegalArgumentException(StringUtils.substituteParameters(message, previous.getName()));
        }

        if (!Objects.equals(previous.getCode(), country.getCode())) {
            String message = MessageService.getMessage("common.world-country.edit.not-allowed.code");
            throw new IllegalArgumentException(StringUtils.substituteParameters(message, previous.getName()));
        }
    }

    // Alias: ПроверитьУникальностьЭлементов
    private List<ErrorMessage> checkEntityUniqueness(WorldCountry country) {
        List<ErrorMessage> result = new ArrayList<>();

        String codeQuery;
        if (country.getCode().equals("0") || country.getCode().equals("00") || country.getCode().equals("000")) {
            codeQuery = "000";
        } else {
            codeQuery = String.format("%d", Integer.parseUnsignedInt(country.getCode(), 0, 3, 10));
        }

        List<WorldCountry> countriesToCheck = findDuplicatesQuery(codeQuery, country);
        if (countriesToCheck.isEmpty()) {
            // Все ок, пропускаем
            return result;
        }

        for (WorldCountry countryCheck : countriesToCheck) {
            String message = null, fieldName = null;

            if (Objects.equals(country.getCode(), countryCheck.getCode())) {
                message = StringUtils.substituteParameters(
                        MessageService.getMessage("common.world-country.exists.code"),
                        country.getCode(), countryCheck.getName());
                fieldName = "code";
            } else if (Objects.equals(country.getName(), countryCheck.getName())) {
                message = StringUtils.substituteParameters(
                        MessageService.getMessage("common.world-country.exists.name"),
                        countryCheck.getName());
                fieldName = "name";
            } else if (Objects.equals(country.getFullName(), countryCheck.getFullName())) {
                message = StringUtils.substituteParameters(
                        MessageService.getMessage("common.world-country.exists.full-name"),
                        country.getFullName(), countryCheck.getName());
                fieldName = "fullName";
            } else if (Objects.equals(country.getCodeAlpha2(), countryCheck.getCodeAlpha2())) {
                message = StringUtils.substituteParameters(
                        MessageService.getMessage("common.world-country.exists.code-alpha-2"),
                        country.getCodeAlpha2(), countryCheck.getName());
                fieldName = "codeAlpha2";
            } else if (Objects.equals(country.getCodeAlpha3(), countryCheck.getCodeAlpha3())) {
                message = StringUtils.substituteParameters(
                        MessageService.getMessage("common.world-country.exists.code-alpha-3"),
                        country.getCodeAlpha3(), countryCheck.getName());
                fieldName = "codeAlpha3";
            }

            if (message != null) {
                result.add(new ErrorMessage(message, fieldName));
            }
        }

        return result;
    }

    // Получаем список стран, значение которых может совпадать с текущими
    private List<WorldCountry> findDuplicatesQuery(String codeQuery, WorldCountry country) {
        return worldCountryRepository.findDuplicates(codeQuery, country.getName(),
            country.getCodeAlpha2(), country.getCodeAlpha3(), country.getFullName(), country.getId());
    }

    public record ErrorMessage(String message, String fieldName) {}
}
