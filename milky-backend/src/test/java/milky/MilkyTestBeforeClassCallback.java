package milky;

import io.quarkus.test.junit.callback.QuarkusTestBeforeClassCallback;
import org.jboss.logging.Logger;

public class MilkyTestBeforeClassCallback implements QuarkusTestBeforeClassCallback {

    private static final Logger LOGGER = Logger.getLogger(MilkyTestBeforeClassCallback.class);

    @Override
    public void beforeClass(Class<?> testClass) {
        LOGGER.info("@QuarkusTest Class will be started to run tests");
    }
}
