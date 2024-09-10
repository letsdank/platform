package net.letsdank.platform.module.salary.hr.pr.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement
@Getter
@Setter
public class HRPR_QueryDescription {
    private boolean distinct = false;
    private String tableToPlace = "";
    private List<String> columns = new ArrayList<>(); // TODO: Is it String?
    private final List<String> order = new ArrayList<>();
    private final List<HRPR_QueryDescriptionOperator> operators = new ArrayList<>();
    private final List<String> indexFields = new ArrayList<>();
}
