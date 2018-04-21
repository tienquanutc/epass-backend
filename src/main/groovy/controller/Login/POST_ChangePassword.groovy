package controller.Login

import app.AppConfig
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import model.User
import mongodb.collection.UserCollection
import org.apache.commons.lang3.Validate
import utils.EPassValidate
import utils.Password
import vertx.JsonResponse
import vertx.VertxController

@Slf4j
class POST_ChangePassword extends VertxController<AppConfig> {

    UserCollection userCollection

    POST_ChangePassword(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {
        def json = parseJson(context)
        EPassValidate.isMD5Hash(json.old_password as String, "old password must be a md5 hash")
        EPassValidate.isMD5Hash(json.new_password as String, "new password must be a md5 hash")
        context.put("old_password", json.old_password)
        context.put("new_password", json.new_password)
        String userId = context.user().principal().getString("_id")
        context.put("_id", userId)

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def databaseId = context.get("_id") as String
        def oldPassword = context.get("old_password") as String
        def newPassword = context.get("new_password") as String

        def userUpdated = userCollection.changePassword(databaseId, Password.hash(oldPassword), newPassword)
        def jsonResponse = new JsonResponse<User>(data: userUpdated)

        writeJson(response, 200, jsonResponse)
    }
}
