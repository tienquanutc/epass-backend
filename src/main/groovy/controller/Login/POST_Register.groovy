package controller.Login

import app.AppConfig
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import model.User
import mongodb.collection.UserCollection
import utils.EPassValidate
import utils.Password
import vertx.JsonResponse
import vertx.VertxController

@Slf4j
class POST_Register extends VertxController<AppConfig> {

    private UserCollection userCollection

    POST_Register(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        def json = parseJson(context)
        EPassValidate.notNull(json?.username, "username must be not null or white space")
        EPassValidate.isMD5Hash(json.password as String, "password must be a md5 hash")
        EPassValidate.isEmail(json.email as String, "not invalid email")

        def username = json.username as String
        def email = json.email as String
        userCollection.verify(username, email)

        context.put('username', username)
        context.put('hash_password', Password.hash(json.password as String))
        context.put('email', email)
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def userName = context.get('username') as String
        def hashPassword = context.get('hash_password') as String
        def email = context.get('email') as String

        User user = new User().username(userName).hashPassword(hashPassword).salt(hashPassword).email(email)
        def resultUser = userCollection.insert(user)
        def jsonResponse = new JsonResponse<User>().data(resultUser)
        writeJson(response, 200, jsonResponse)
    }
}
