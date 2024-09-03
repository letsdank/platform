package net.letsdank.platform.service.common;

import net.letsdank.platform.entity.common.WorldCountry;
import net.letsdank.platform.model.common.PlatformResult;
import net.letsdank.platform.repository.common.WorldCountryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

public class WorldCountryServiceTest {
    @Mock
    private WorldCountryRepository worldCountryRepository;

    @InjectMocks
    private WorldCountryService worldCountryService;

    // Проверяем, что мы не можем менять наименование и код предопределенной страны
    @Test
    public void testCheckPredefinedValue() {
        MockitoAnnotations.openMocks(this);

        // Страна предыдущие данные
        WorldCountry country = new WorldCountry();
        country.setId(1L);
        country.setPredefined(true);
        country.setName("РОССИЯ");
        country.setCode("643");

        when(worldCountryRepository.getReferenceById(1L)).thenReturn(country);

        WorldCountry newCountry = new WorldCountry();
        newCountry.setId(1L);
        newCountry.setPredefined(true);
        newCountry.setCode("643");

        // Редактируем название страны
        newCountry.setName("РОССИЯАА");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> worldCountryService.save(newCountry));

        String expectedMessage = "Не допускается изменение наименования для страны РОССИЯ";
        assertEquals(expectedMessage, exception.getMessage());

        // Редактируем код страны
        newCountry.setName("РОССИЯ");
        newCountry.setCode("644");

        exception = assertThrows(IllegalArgumentException.class, () -> worldCountryService.save(newCountry));

        expectedMessage = "Не допускается изменение кода для страны РОССИЯ";
        assertEquals(expectedMessage, exception.getMessage());
    }

    // Проверяем, что мы не можем добавлять дублирующие данные
    @Test
    public void testDataUniqueness() {
        MockitoAnnotations.openMocks(this);

        // Страна существующие данные
        List<WorldCountry> queryResult = getWorldCountries();

        when(worldCountryRepository.findDuplicates(anyString(), anyString(), anyString(), anyString(), anyString(), isNull()))
                .thenReturn(queryResult);

        // Новая страна, все данные уникальны
        WorldCountry newCountry = new WorldCountry();
        newCountry.setId(null);
        newCountry.setName("СЕРБИЯ");
        newCountry.setCode("688");
        newCountry.setFullName("Республика Сербия");
        newCountry.setCodeAlpha2("RS");
        newCountry.setCodeAlpha3("SRB");

        assertTrue(worldCountryService.save(newCountry).isSuccess());

        // Меняем название
        newCountry.setName("РОССИЯ");
        PlatformResult result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals("name", result.getErrors().get(0).fieldName());

        // Меняем код
        newCountry.setName("СЕРБИЯ");
        newCountry.setCode("643");
        result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals("code", result.getErrors().get(0).fieldName());

        // Меняем полное название
        newCountry.setCode("688");
        newCountry.setFullName("Российская Федерация");
        result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals("fullName", result.getErrors().get(0).fieldName());

        // Меняем код Альфа-2
        newCountry.setFullName("Республика Сербия");
        newCountry.setCodeAlpha2("RU");
        result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals("codeAlpha2", result.getErrors().get(0).fieldName());

        // Меняем код Альфа-3
        newCountry.setCodeAlpha2("RS");
        newCountry.setCodeAlpha3("RUS");
        result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals("codeAlpha3", result.getErrors().get(0).fieldName());

        // Выбиваем случай, когда 2 совпадения у разных значений
        newCountry.setCodeAlpha2("DE");
        result = worldCountryService.save(newCountry);
        assertFalse(result.isSuccess());
        assertEquals(2, result.getErrors().size());
        assertEquals("codeAlpha3", result.getErrors().get(0).fieldName());
        assertEquals("codeAlpha2", result.getErrors().get(1).fieldName());
    }

    // Мок данных
    private static List<WorldCountry> getWorldCountries() {
        WorldCountry country1 = new WorldCountry();
        country1.setId(1L);
        country1.setName("РОССИЯ");
        country1.setCode("643");
        country1.setFullName("Российская Федерация");
        country1.setCodeAlpha2("RU");
        country1.setCodeAlpha3("RUS");
        WorldCountry country2 = new WorldCountry();
        country2.setId(2L);
        country2.setName("ГЕРМАНИЯ");
        country2.setCode("276");
        country2.setFullName("Федеративная Республика Германия");
        country2.setCodeAlpha2("DE");
        country2.setCodeAlpha3("DEU");

        List<WorldCountry> queryResult = new ArrayList<>();
        queryResult.add(country1);
        queryResult.add(country2);
        return queryResult;
    }
}
