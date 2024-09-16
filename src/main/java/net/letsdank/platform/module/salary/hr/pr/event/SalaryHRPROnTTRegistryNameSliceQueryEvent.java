package net.letsdank.platform.module.salary.hr.pr.event;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SalaryHRPROnTTRegistryNameSliceQueryEvent extends ApplicationEvent {
    private SQLQuery query;
    private String registryName;
    private boolean onlyDistrict;
    private Object filter; // TODO: To OOP
    private TTRegistryNameBuildContext buildContext;
    private boolean isSliceLast;
    private String resultTTName;

    public SalaryHRPROnTTRegistryNameSliceQueryEvent(Object source, SQLQuery query, String registryName, boolean onlyDistrict,
                                                     Object filter, TTRegistryNameBuildContext buildContext, boolean isSliceLast,
                                                     String resultTTName) {
        super(source);

        this.query = query;
        this.registryName = registryName;
        this.onlyDistrict = onlyDistrict;
        this.filter = filter;
        this.buildContext = buildContext;
        this.isSliceLast = isSliceLast;
        this.resultTTName = resultTTName;
    }
}
