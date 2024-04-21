package net.letsdank.platform.model.common;

import java.util.ArrayList;
import java.util.List;

public class PlatformResult {
    private List<ErrorMessage> errors;

    public PlatformResult() {
        this.errors = new ArrayList<>();
    }

    public void addError(ErrorMessage error) {
        errors.add(error);
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
