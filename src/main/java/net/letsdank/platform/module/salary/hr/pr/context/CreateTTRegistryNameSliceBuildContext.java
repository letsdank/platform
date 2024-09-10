package net.letsdank.platform.module.salary.hr.pr.context;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateTTRegistryNameSliceBuildContext extends TTRegistryNameBuildContext {
    // TODO: JavaDoc
    private boolean allRecords;
    private boolean includeBorder;
    private final List<String> filterApplyingToSlice;

    // Alias: ПараметрыПостроенияДляСоздатьВТИмяРегистраСрез
    public CreateTTRegistryNameSliceBuildContext() {
        super();

        allRecords = false;
        includeBorder = true;
        filterApplyingToSlice = new ArrayList<>();
    }
}
