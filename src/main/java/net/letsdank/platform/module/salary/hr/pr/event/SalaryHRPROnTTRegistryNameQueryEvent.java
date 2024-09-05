package net.letsdank.platform.module.salary.hr.pr.event;

import lombok.Getter;
import lombok.Setter;
import net.letsdank.platform.module.salary.hr.pr.context.TTRegistryNameBuildContext;
import net.letsdank.platform.utils.platform.sql.SQLQuery;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SalaryHRPROnTTRegistryNameQueryEvent extends ApplicationEvent {
    private SQLQuery query;
    private String registryName;
    private boolean onlyDistrict;
    private Object filter; // TODO: To OOP
    private TTRegistryNameBuildContext buildContext;
    private String resultTTName;

    public SalaryHRPROnTTRegistryNameQueryEvent(Object source, SQLQuery query, String registryName, boolean onlyDistrict,
                                                Object filter, TTRegistryNameBuildContext buildContext, String resultTTName) {
        super(source);
        this.query = query;
        this.registryName = registryName;
        this.onlyDistrict = onlyDistrict;
        this.filter = filter;
        this.buildContext = buildContext;
        this.resultTTName = resultTTName;
    }
}
