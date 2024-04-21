package net.letsdank.platform.model.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlatformResult {
    private List<ErrorMessage> errors;

    public PlatformResult() {
        this.errors = new ArrayList<>();
    }

    public void addError(ErrorMessage error) {
        errors.add(error);
    }

    public void addError(String message, String fieldName) {
        errors.add(new ErrorMessage(message, fieldName));
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
