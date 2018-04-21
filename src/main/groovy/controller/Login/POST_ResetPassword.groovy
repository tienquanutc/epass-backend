package controller.Login

import app.AppConfig
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Jwts
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.jwt.JWTOptions
import io.vertx.ext.auth.jwt.impl.JWT
import io.vertx.ext.web.RoutingContext
import model.User
import mongodb.collection.UserCollection
import utils.EPassValidate
import utils.Gmail
import vertx.JsonError
import vertx.JsonResponse
import vertx.VertxController

@Slf4j
class POST_ResetPassword extends VertxController<AppConfig> {

    private UserCollection userCollection

    POST_ResetPassword(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        def json = parseJson(context)

        EPassValidate.notBlank(json.username as String, "username must be not null or white space")
        EPassValidate.isEmail(json.email as String, "invalid email")

        context.put('username', json.username)
        context.put('email', json.email)
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def username = context.get('username') as String
        def email = context.get('email') as String

        User user = userCollection.findByUsername(username)
        if (!user || user.email != email) {
            def userNotFoundResponse = new JsonError("username or email invalid")
            writeJson(response, 200, userNotFoundResponse)
        }

        def claims = ["username": username, "_id": user.databaseId] as JsonObject
        def options = new JWTOptions(algorithm: "RS256", expiresInMinutes: 3L, permissions: ['reset_password'])
        def token = config.authProviderWithKeyStore.generateToken(claims, options)
        Gmail.sendRecoveryMailAsync(user, token).thenRun {
            def jsonResponse = new JsonResponse().data(['message': "check mail $email".toString()])
            writeJson(response, 200, jsonResponse)
        }
    }
}
