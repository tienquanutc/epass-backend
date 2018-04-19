package controller

import app.AppConfig
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheckRegistry
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.VertxController

/**
 * Created by dangnh@eway.vn on 2/4/17.
 */
@Slf4j
class HEALTH_CHECK extends VertxController<AppConfig> {

    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry()

    HEALTH_CHECK(AppConfig config) {
        super(config)
//        healthCheckRegistry.with {
//            register("mongodb-offers", new MongoHealthCheck(config.offersCollection))
//            register("es-transactions", new EsClusterHealthCheck(config.transactionsCollection.delegate))
//            register("es-products", new EsClusterHealthCheck(config.productsCollection.delegate))
//        }
    }

    @Override
    void validate(RoutingContext context, HttpServerRequest request) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        config.vertx.executeBlocking({blockingCode ->
            SortedMap<String, HealthCheck.Result> healthCheckResult = healthCheckRegistry.runHealthChecks()
            healthCheckResult.values().each {result ->
                if (!result.isHealthy()){
                    response.setStatusCode(500)
                }
            }

            response.end(mapper.writeValueAsString(healthCheckResult))
            blockingCode.complete()
        }, {asyncResult ->
            if (asyncResult.failed()){
                log.error("@@HeathCheck handler failure", asyncResult.cause())
                response.setStatusCode(500).end()
            }
        })
    }

}
