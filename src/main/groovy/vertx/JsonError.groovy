package vertx

import io.vertx.core.json.Json
import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * Created by chipn@eway.vn@eway.vn on 7/14/2016.
 */
class JsonError {

    String message
    String rootCauseMessage
    String detail

    JsonError() {
    }

    JsonError(Throwable throwable, boolean debug) {
        this.message = ExceptionUtils.getMessage(throwable)
        this.rootCauseMessage = ExceptionUtils.getRootCauseMessage(throwable)
        if (debug) {
            this.detail = ExceptionUtils.getStackTrace(throwable)
        }
    }

    JsonError(String message) {
        this.message = message
    }

    JsonError message(String message) {
        this.message = message
        return this
    }

    JsonError rootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage
        return this
    }

    JsonError detail(String detail) {
        this.detail = detail
        return this
    }
}
