package controller.Login

import app.AppConfig
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.jwt.JWTOptions
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.Validate
import org.bson.Document
import utils.EPassValidate
import utils.Password
import vertx.JsonResponse
import vertx.VertxController


@CompileStatic
@InheritConstructors
@Slf4j
class GET_Login extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        EPassValidate.notBlank(context.request().getParam("username"), "username param must not be blank")
        EPassValidate.notBlank(context.request().getParam("password"), "password param must not be blank")
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def username = request.getParam("username")
        def hash_password = request.getParam("password")

        def filter = [
                username     : username,
                hash_password: Password.hash(hash_password)
        ] as Document

        def user = config.userCollection.findModels(filter).intoModels().join()[0]
        if (!user) {
            writeJson(response, 200, [message: "username or password not match"])
            return
        }


        def claims = ["username": username, "_id": user.databaseId] as JsonObject
        def options = new JWTOptions(algorithm: "RS256", permissions: ["read-test", "write-test"], expiresInMinutes: 10L)
        def jwtToken = config.authProviderWithKeyStore.generateToken(claims, options)
//        def jwtToken = config.authProviderWithPublicKey.generateToken(claims, options)
        def jsonResponse = new JsonResponse<Map>().data(["token": jwtToken])
        writeJson(response, 200, jsonResponse)
    }
}
