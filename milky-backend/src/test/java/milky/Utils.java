package milky;

import java.util.UUID;

public class Utils {

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}
