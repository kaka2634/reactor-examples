package reactor.examples.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private final static Logger defaultLogger = LoggerFactory.getLogger(LoggerUtil.class);


    public static <T> void logInfo(T data) {
        logInfo(defaultLogger, data);
    }

    public static <T> T logInfoAndReturn(T data) {
        return logInfoAndReturn(defaultLogger, data);
    }

    public static <T> void logInfo(Logger logger, T data) {
        logger.info(String.valueOf(data));
    }


    public static <T> T logInfoAndReturn(Logger logger, T data) {
        logger.info(String.valueOf(data));
        return data;
    }

    public static <T> void logInfo(Logger logger, String format, T data) {
        logger.info(format, String.valueOf(data));
    }

    public static <T> T logInfoAndReturn(Logger logger, String format, T data) {
        logger.info(format, String.valueOf(data));
        return data;
    }


}
