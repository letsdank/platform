package net.letsdank.platform.service.product;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Map;

public class UnitClassifierServiceTest {
    @InjectMocks
    private UnitClassifierService unitClassifierService;

    @Test
    public void testUnitClassifierMap() {
        List<Map<String, Object>> map = unitClassifierService.getUnitClassifierAsMap();
        return;
    }
}
