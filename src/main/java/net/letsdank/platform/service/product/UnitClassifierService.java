package net.letsdank.platform.service.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.product.UnitClassifier;
import net.letsdank.platform.entity.vetis.MeasuredUnitType;
import net.letsdank.platform.repository.product.UnitClassifierRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UnitClassifierService {
    private final UnitClassifierRepository unitClassifierRepository;

    // Функция возвращает дерево значений с данными ОКЕИ.
    // Alias: ПолучитьДанныеКлассификатора
    public Map<String, Object> getUnitClassifierData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("data/entity/unit-classifier.json");

            return objectMapper.readValue(resource.getInputStream(), new TypeReference<>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выполняет поиск единицы измерения в справочнике {@code UnitClassifier}.
     * Если элемент в справочнике не найден, осуществляется попытка добавления
     * элемента из ОКЕИ, если в ОКЕИ элемент не найден, то он не создается!
     *
     * @param okeiCode Код единицы измерения по ОКЕИ
     * @return {@code UnitClassifier}, если поиск успешен, иначе {@code null}.
     */
    public UnitClassifier getUnitByCode(String okeiCode) {
        if (okeiCode == null) {
            return null;
        }

        // Сначала попытаемся найти единицу среди уже существующих в справочнике
        Optional<UnitClassifier> optional = unitClassifierRepository.findByCode(okeiCode);
        if (optional.isPresent()) {
            // Если нашли, возвращаем
            return optional.get();
        }

        List<Map<String, Object>> data = getUnitClassifierAsMap();
        Map<String, Object> foundEntity = data.stream()
                .filter(map -> map.get("code").equals(okeiCode))
                .findFirst()
                .orElse(null);

        if (foundEntity != null) {
            UnitClassifier newClassifier = fillUnitObject(new UnitClassifier(), foundEntity);
            return unitClassifierRepository.save(newClassifier);
        }

        return null;
    }

    public List<Map<String, Object>> getUnitClassifierAsMap() {
        Map<String, Object> data = getUnitClassifierData();
        return convertClassifierTreeToMap(data);
    }

    // Alias: ПреобразованиеДеревоКлассификатораЕдиницВТаблицу
    private List<Map<String, Object>> convertClassifierTreeToMap(Map<String, Object> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object value : data.values()) {
            if (value instanceof List) {
                result.addAll(convertClassifierTreeToMap((List<Map<String, Object>>) value));
                continue;
            } else if (!(value instanceof Map)) {
                // Должна быть строка, тогда добавляем все
                result.add(data);
                break;
            }

            Map<String, Object> map = (Map<String, Object>) value;
            if (map.containsKey("code")) {
                result.add(map);
                continue;
            }

            result.addAll(convertClassifierTreeToMap(map));
        }

        return result;
    }

    // Alias: ПреобразованиеДеревоКлассификатораЕдиницВТаблицу
    private List<Map<String, Object>> convertClassifierTreeToMap(List<Map<String, Object>> data) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> value : data) {
            result.addAll(convertClassifierTreeToMap(value));
        }

        return result;
    }

    // Alias: ЗаполнитьОбъектЕдиницыИзмерения
    private UnitClassifier fillUnitObject(UnitClassifier unit, Map<String, Object> data) {
        unit.setName(getUnitName(data));
        unit.setInternationalAbbr(((String) data.get("internationalCodeAbbreviation")).replace('\n', '/'));
        unit.setFullName(((String) data.get("name")).replace('\n', '/'));
        unit.setCode(((String) data.get("code")).trim());

        if (data.get("measuredUnitType") != null) {
            unit.setMeasuredUnitType(MeasuredUnitType.valueOf((String) data.get("measuredUnitType")));
        }

        return unit;
    }

    // Alias: НаименованиеЕдиницыИзмерения
    private String getUnitName(Map<String, Object> data) {
        String result;
        if (null != data.get("abbreviation"))                       result = (String) data.get("abbreviation");
        else if (null != data.get("internationalAbbreviation"))     result = (String) data.get("internationalAbbreviation");
        else if (null != data.get("codeAbbreviation"))              result = (String) data.get("codeAbbreviation");
        else if (null != data.get("internationalCodeAbbreviation")) result = (String) data.get("internationalCodeAbbreviation");
        else result = (String) data.get("name");

        return result.replace('\n', '/');
    }
}
