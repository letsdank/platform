package net.letsdank.platform.service.email;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class EmailCheckerResult {
    private final List<String> executedCheckes = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();
    private final List<String> technicalDetails = new ArrayList<>();

    public void addExecutedCheck(String check) {
        executedCheckes.add(check);
    }

    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    public void addTechnicalDetails(String message) {
        technicalDetails.add(message);
    }
}
