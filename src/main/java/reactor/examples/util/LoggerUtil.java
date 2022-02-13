package reactor.examples.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    public static <T> void logInfo(Logger logger, T data) {
        logger.info(String.valueOf(data));
    }

    public static <T> T logInfoAndReturn(Logger logger, T data) {
        logger.info(String.valueOf(data));
        return data;
    }

    public static <T> void logInfo(Logger logger, String format, T data) {
        logger.info(format, data);
    }

    public static <T> T logInfoAndReturn(Logger logger, String format, T data) {
        logger.info(format, data);
        return data;
    }


}
