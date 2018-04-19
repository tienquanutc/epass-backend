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
appender("ACCESS-LOG", RollingFileAppender) {
    append = true
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = ".vertx/log/access.log.%d{yyyy-MM-dd}.%i"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = "100MB"
        }
        maxHistory = 30
    }
    encoder(LayoutWrappingEncoder) {
        charset = Charset.forName("UTF-8")
        layout(PatternLayout) {
            pattern = "%msg%n"
        }
    }
}
appender("Async", AsyncAppender) {
    discardingThreshold = 0
    queueSize = 5000
    appenderRef("ACCESS-LOG")
}
appender("Sentry", SentryAppender) {
    dsn = "http://7806ff66dea1417c951771cbcd0787e4:284f83f804094c6dbd902e57fc4bdd91@sentry.eway.vn/39"
    filter(ThresholdFilter) {
         level = WARN
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

root(DEBUG, ["STDOUT", "Sentry"])