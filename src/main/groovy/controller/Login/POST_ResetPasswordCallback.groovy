package controller.Login

import app.AppConfig
import groovy.json.JsonSlurper
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
public class POST_ResetPasswordCallback extends VertxController<AppConfig> {

    UserCollection userCollection

    public POST_ResetPasswordCallback(AppConfig config) {
        super(config)
        userCollection = config.userCollection
    }

    @Override
    public void validate(RoutingContext context, HttpServerRequest request) {
        def json = parseJson(context)
        EPassValidate.notBlank(json.token as String, 'token required')
        EPassValidate.isMD5Hash(json.new_password as String, 'new password must be a md5 hash')
        context.put('token', json.token as String)
        context.put('new_password', json.new_password as String)
    }

    @Override
    public void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        def token = context.get('token') as String
        def newPassword = context.get('new_password') as String

        def jsonObject = new JsonObject(['jwt': token])
        config.authProviderWithKeyStore.authenticate(jsonObject) { event ->
            def jwtParsed = new JsonSlurper().parseText(event?.result()?.principal()?.toString() ?: '{"error":true}')
            if (jwtParsed.error)
                throw new RuntimeException('invalid token')
            if (!('reset_password' in jwtParsed.permissions))
                throw new RuntimeException('token not have permissions reset password')
            def _id = jwtParsed._id as String
            if (!_id)
                throw new RuntimeException('invalid requests')
            def user = userCollection.findById(_id)
            if (!user)
                throw new RuntimeException('invalid user')
            if ((user.updatedAt.time / 1000 as Long) > event.result().principal().getLong('iat')) {
                log.info("token.iat >= user.updated_at")
                throw new RuntimeException('token invalidate')
            }
            userCollection.changePassword(_id, Password.hash(newPassword))
            def responseJson = new JsonResponse().data([new_password: newPassword])
            writeJson(response, 200, responseJson)
        }
    }
}
