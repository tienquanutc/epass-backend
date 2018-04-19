package vertx

import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.Vertx

/**
 * Created by chipn@eway.vn on 2/5/17.
 */
class VertxConfig {

    @JsonProperty("http.port")
    int httpPort

    Vertx vertx = Vertx.vertx()
}
