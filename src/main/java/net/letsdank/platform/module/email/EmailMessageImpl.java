package net.letsdank.platform.module.email;

import net.letsdank.platform.module.email.utils.PunycodeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Alias:
// РаботаСПочтовымиСообщениями
// РаботаСПочтовымиСообщениямиСлужебный
public class EmailMessageImpl {

    public static String convertToPunycode(String input) {
        return PunycodeUtils.convertToPunycode(input);
    }

    public static String punycodeToString(String input) {
        return PunycodeUtils.punycodeToString(input);
    }

    // Alias: АдресаСерверовDNS
    public static List<String> getDnsServers() {
        // TODO: Implement interface, then use class that inherited from interface.
        return new ArrayList<>(Arrays.asList("8.8.8.8", "8.8.4.4")); // dns.google
    }
}
