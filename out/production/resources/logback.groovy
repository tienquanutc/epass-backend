import ch.qos.logback.classic.AsyncAppender
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.NopStatusListener
import ch.qos.logback.classic.filter.ThresholdFilter

import com.getsentry.raven.logback.SentryAppender
import java.nio.charset.Charset
import static ch.qos.logback.classic.Level.ALL
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.WARN

statusListener(NopStatusListener)
appender("STDOUT", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{100} - %msg%n"
    }
}

logger("io.vertx.ext.web.handler.impl.LoggerHandlerImpl", ALL, ["Async"], false)
logger("io.netty", WARN)
logger("io.vertx", WARN)
logger("org.apache.shiro", WARN)
logger("org.mongodb.driver", WARN)
logger("org.elasticsearch", WARN)
logger("org.apache.activemq", WARN)
logger("com.github.jknack.handlebars.internal.HbsParserFactory", WARN)

root(DEBUG, ["STDOUT"])