package app

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Jwts
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.jwt.JWTAuth
import mongodb.collection.UserCollection
import org.apache.commons.lang3.Validate
import vertx.VertxConfig


@Slf4j
class AppConfig extends VertxConfig {

    @JsonProperty("jwt_auth_config1")
    @JsonDeserialize(as = HashMap)
    Map<String, Object> jwtAuthConfig1

    @JsonProperty("jwt_auth_config2")
    @JsonDeserialize(as = HashMap)
    Map<String, Object> jwtAuthConfig2

    @Lazy
    JWTAuth authProviderWithKeyStore = JWTAuth.create(vertx, jwtAuthConfig1 as JsonObject)

    @Lazy
    JWTAuth authProviderWithPublicKey = JWTAuth.create(vertx, jwtAuthConfig2 as JsonObject)

    @JsonProperty("user.mongodb.uri")
    UserCollection userCollection

    static AppConfig newInstance(File appConfigFile) throws IOException {
        Validate.isTrue(appConfigFile.exists(), "AppConfigFile not exists: ${appConfigFile.getAbsolutePath()}")
        log.debug('@Loading app.AppConfig')

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()).with {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            disable DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        }

        AppConfig appConfig = objectMapper.readValue(appConfigFile, AppConfig.class)
//        appConfig.properties.each {Validate.notNull(it.value, "${it.key} must not be null")}

        //=> Overide http.port properties from System Properties
        String httpPort = System.properties.getProperty('http.port', Integer.toString(appConfig.httpPort))
        appConfig.httpPort = Integer.parseInt(httpPort)
        return appConfig
    }
}
