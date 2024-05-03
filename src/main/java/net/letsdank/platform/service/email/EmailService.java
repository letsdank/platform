package net.letsdank.platform.service.email;

import java.util.Arrays;
import java.util.List;

// Aliases:
// РаботаСПочтовымиСообщениямиСлужебный
public class EmailService {

    public static List<String> getDnsServers() {
        // TODO: РаботаСПочтовымиСообщениямиСлужебныйЛокализация
        return Arrays.asList("8.8.8.8", "8.8.4.4"); // dns.google
    }

}
