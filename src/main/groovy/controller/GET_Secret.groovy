package controller

import app.AppConfig
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.VertxController

@Slf4j
class GET_Secret extends VertxController<AppConfig> {
    GET_Secret(AppConfig config) {
        super(config)
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        writeJson(response, 200, [warning: 'this is secret area'])
    }
}
