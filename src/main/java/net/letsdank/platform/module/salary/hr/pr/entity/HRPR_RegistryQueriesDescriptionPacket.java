package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.SQLQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HRPR_RegistryQueriesDescriptionPacket {
    private HRPR_QueryDescription initFiltersQueryDescription;
    private SQLQuery destroyFilterQuery;
    private List<Either<SQLQuery, HRPR_QueryDescription>> dataQueries;
    private Map<String, Object> parameters;

    public HRPR_RegistryQueriesDescriptionPacket() {
        dataQueries = new ArrayList<>();
        parameters = new HashMap<>();
    }
}
