package net.letsdank.platform.module.net;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InternetConnectionDiagnosticResult {
    private String errorDescription;
    private String diagnosticLog;
}
