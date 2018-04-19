package controller

import app.AppConfig
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.VertxController

@InheritConstructors
class GET_HelloWorld extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context, HttpServerRequest request) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        writeJson(response, 200, [message: 'hello world'])
    }
}
