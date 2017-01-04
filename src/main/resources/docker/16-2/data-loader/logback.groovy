import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN

scan()
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
logger("org.apache.zookeeper", WARN)
logger("org.apache.kafka", WARN)
logger("kafka", WARN)
logger("org.I0Itec", WARN)
logger("com.mchange", WARN)
logger("com.nominum.cc", INFO)
logger("com.nominum", DEBUG)
logger("com.zaxxer.hikari", WARN)
root(INFO, ["STDOUT"])
