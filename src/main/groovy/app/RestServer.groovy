package app

import controller.*
import controller.Login.POST_ResetPasswordCallback
import controller.Login.GET_Login
import controller.Login.POST_ChangePassword
import controller.Login.POST_ResetPassword
import controller.Login.POST_Register
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Route
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.FaviconHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.impl.Utils
import vertx.VertxServer

import static io.netty.handler.codec.http.HttpMethod.*

/**
 * Created by chipn@eway.vn on 1/25/17.
 */
@Slf4j
class RestServer extends VertxServer<AppConfig> {
    static {
        Route.metaClass.rightShift = { Class clazz ->
            return delegate.handler(clazz.newInstance(config))
        }
    }

    RestServer(AppConfig config) {
        super(config)
    }

    @Override
    setupRouter() {
        Route.metaClass.rightShift = { Class clazz ->
            return delegate.handler(clazz.newInstance(config))
        }


        route().handler(BodyHandler.create())
        route().handler(CookieHandler.create())
        if (Utils.readResourceToBuffer("favicon.ico") != null) {
            route().handler(FaviconHandler.create())
        }
        route().handler(LoggerHandler.create(true, LoggerFormat.DEFAULT))

        route().last().handler(new NOT_FOUND(config))
        route().failureHandler(new INTERNAL_SERVER_ERROR(config))

        route("/static/*").handler(StaticHandler.create("webroot").with {
            setCachingEnabled(false)
        })

        route("/v1/*").handler(JWTAuthHandler.create(config.authProviderWithPublicKey))

        route("/v1/*").handler(CorsHandler.create("*").with {
            allowedMethods([OPTIONS, GET, POST, PUT, DELETE, PATCH] as Set<HttpMethod>)
            allowCredentials(true)
            allowedHeaders(['Authorization', 'Access-Control-Allow-Method', 'Access-Control-Allow-Headers', 'Content-Type'] as Set)
        })

        get("/login") >> GET_Login
        post("/register") >> POST_Register
        post("/v1/change-password") >> POST_ChangePassword
        post("/reset-password") >> POST_ResetPassword
        post("/reset-password-callback") >> POST_ResetPasswordCallback
        get("/v1/secret") >> GET_Secret

        route('/health.json').handler(new HEALTH_CHECK(config))
    }

}
