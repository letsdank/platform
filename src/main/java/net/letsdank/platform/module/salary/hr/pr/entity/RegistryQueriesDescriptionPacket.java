package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.utils.data.Either;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import net.letsdank.platform.utils.platform.sql.description.QueryDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RegistryQueriesDescriptionPacket {
    private QueryDescription initFiltersQueryDescription;
    private SQLQuery destroyFilterQuery;
    private List<Either<SQLQuery, QueryDescription>> dataQueries;
    private Map<String, Object> parameters;

    public RegistryQueriesDescriptionPacket() {
        dataQueries = new ArrayList<>();
        parameters = new HashMap<>();
    }
}
