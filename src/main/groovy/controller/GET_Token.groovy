package controller

import app.AppConfig
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.jwt.JWTOptions
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.Validate
import vertx.JsonResponse
import vertx.VertxController


@CompileStatic
@InheritConstructors
class GET_Token extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        Validate.notBlank(context.request().getParam("username"), "username param must not be blank")
        Validate.notBlank(context.request().getParam("password"), "password param must not be blank")
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def username = request.getParam("username")
        def password = request.getParam("password")

        //TODO: handler authorized
        if (password != "test") {
            throw new Exception("password incorrect")
        }

        def claims = ["username": username] as JsonObject
        def options = new JWTOptions(algorithm: "RS256", permissions: ["read-test", "write-test"], expiresInMinutes: 10L)
        def jwtToken = config.authProviderWithKeyStore.generateToken(claims, options)
//        def jwtToken = config.authProviderWithPublicKey.generateToken(claims, options)

        def jsonResponse = new JsonResponse<Map>()
        jsonResponse.setData([
                "token": jwtToken
        ])

        writeJson(response, 200, jsonResponse)
    }

}
