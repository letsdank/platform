package net.letsdank.platform.service.email;

import net.letsdank.platform.utils.string.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EmailAccountService {

    // Alias: ОпределитьИменаПочтовыхСерверовДомена
    public static List<String> determineMailServersForDomain(String domain) {
        List<String> result = new ArrayList<>();

        List<String> dnsServers = EmailService.getDnsServers();
        dnsServers.add(0, ""); // Add default DNS server.

        for (String dnsServer : dnsServers) {
            String command = StringUtils.substituteParameters("nslookup -type=mx %1 %2",
                    domain, dnsServer);

            try {
                // TODO: Выписать в отдельный функциональный класс (ФайловаяСистема)
                Process process = Runtime.getRuntime().exec(command);

                BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                StringBuilder output = new StringBuilder();
                while ((line = outputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                while ((line = errorReader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                if (process.waitFor() == 0) {
                    String[] lines = output.toString().split("\n");
                    for (String lineStr : lines) {
                        if (lineStr.contains("mail exchanger")) {
                            String[] parts = lineStr.split(" ");
                            String serverName = parts[parts.length - 1];
                            result.add(serverName);
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                // ignore
            }

            if (!result.isEmpty()) {
                break;
            }
        }

        return result;
    }
}
