package net.letsdank.platform.utils.url;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class URLUtilsTest {
    @Test
    public void test() {
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        URLInfo info = URLUtils.getURLInfo(url);

        assertEquals("https", info.getScheme());
        assertEquals("www.youtube.com", info.getHost());
        assertEquals("/watch?v=dQw4w9WgXcQ", info.getPath());

        url = "ssh://git@github.com:letsdank/platform.git";
        info = URLUtils.getURLInfo(url);

        assertEquals("ssh", info.getScheme());
        assertEquals("github.com", info.getHost());
        assertEquals("git", info.getLogin());
        assertEquals("github.com:letsdank", info.getName());
        assertEquals("/platform.git", info.getPath());
    }
}
