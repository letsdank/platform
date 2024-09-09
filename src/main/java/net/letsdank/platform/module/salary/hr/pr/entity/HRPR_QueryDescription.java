package net.letsdank.platform.module.salary.hr.pr.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement
@Getter
@Setter
public class HRPR_QueryDescription {
    private final List<HRPR_QueryDescriptionOperator> operators = new ArrayList<>(); // TODO: Is it QueryDescriptionOp or QueryDescription?
    private final List<String> order = new ArrayList<>();
    private final List<String> indexFields = new ArrayList<>();
    private String tableToPlace;
    private boolean distinct;
}
