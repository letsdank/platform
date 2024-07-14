package net.letsdank.platform.module.net;

import java.util.ArrayList;
import java.util.List;

public class InternetFileDownloader {
    public static InternetConnectionDiagnosticResult diagnoseConnection(String url) {
        return diagnoseConnection(url, true);
    }

    public static InternetConnectionDiagnosticResult diagnoseConnection(String url, boolean logError) {
        List<String> description = new ArrayList<>();
        description.add("При обращении по URL: " + url);
        description.add(getDiagnosticLocation());

        // TODO: Implement
        if (isDividedEnabled()) {
            description.add("Обратитесь к администратору.");
            return new InternetConnectionDiagnosticResult(String.join("\n", description), "");
        }

        return null;
    }

    private static String getDiagnosticLocation() {
        return "Platform:Web"; // TODO
    }

    private static boolean isDividedEnabled() {
        return true; // TODO: Implement into PlatformUtils (or ОбщегоНазначения)
    }
}
