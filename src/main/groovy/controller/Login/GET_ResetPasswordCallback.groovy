package controller.Login

import app.AppConfig
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import mongodb.collection.UserCollection
import utils.EPassValidate
import utils.Password
import vertx.JsonResponse
import vertx.VertxController

@Slf4j
public class GET_ResetPasswordCallback extends VertxController<AppConfig> {

    UserCollection userCollection

    public GET_ResetPasswordCallback(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    public void validate(RoutingContext context, HttpServerRequest request) {
        EPassValidate.notBlank(request.getParam('token'), 'token required')
    }

    @Override
    public void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def token = request.getParam('token') ?: ''
        def jsonObject = new JsonObject(['jwt': token])
        config.authProviderWithKeyStore.authenticate(jsonObject) { event ->
            def _id = event?.result()?.principal()?.getString('_id')
            if (!_id) {
                throw new RuntimeException("invalid requests")
            }
            def user = userCollection.findById(_id)
            if (!user) {
                throw new RuntimeException("invalid user")
            }
            if ((user.updatedAt.time / 1000 as Long) > event.result().principal().getLong('iat')) {
                throw new RuntimeException("token invalidate")
            }
            def newPassword = Password.generateRandomPassword()
            userCollection.changePassword(_id, Password.hash(newPassword))
            def responseJson = new JsonResponse().data([new_password: newPassword])
            writeJson(response, 200, responseJson)
        }
    }
}
