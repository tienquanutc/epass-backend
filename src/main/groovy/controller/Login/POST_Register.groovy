package controller.Login

import app.AppConfig
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import model.User
import mongodb.collection.UserCollection
import org.apache.commons.lang3.Validate
import org.bson.Document
import utils.Password
import vertx.JsonError
import vertx.JsonResponse
import vertx.VertxController

class POST_Register extends VertxController<AppConfig> {

    private UserCollection userCollection

    POST_Register(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        def json = parseJson(context)
        Validate.notNull(json?.username, "username must be not null or white space")
        Password.Validate.isMD5Hash(json.password as String, "password must be a md5 hash")
        context.put("username", json.username)
        context.put("hash_password", Password.hash(json.password as String))
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def userName = context.get('username') as String
        def hashPassword = context.get('hash_password') as String

        User user = new User().username(userName).hashPassword(hashPassword).salt(hashPassword)
        if (userCollection.isExist(user.username)) {
            def duplicateResponse = new JsonError("username already existed!!")
            writeJson(response, 200, duplicateResponse)
        } else {
            def resultUser = userCollection.insert(user)
            def jsonResponse = new JsonResponse<User>(data: resultUser)
            writeJson(response, 200, jsonResponse)
        }
    }
}
