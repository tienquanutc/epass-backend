package vertx

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.Canonical

/**
 * Created by chipn@eway.vn@eway.vn on 7/13/2016.
 */

@Canonical
class JsonResponse<T> {

    Integer statusCode = 200

    @JsonInclude(JsonInclude.Include.NON_NULL)
    JsonMeta meta = new JsonMeta()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data

    JsonResponse() {
    }

    JsonResponse(Object o) {
        this.data = o
    }


    JsonError error

    JsonResponse<T> data(T data) {
        this.data = data
        return this
    }

    JsonResponse<T> statusCode(Integer statusCode) {
        this.statusCode = statusCode
        return this
    }

    JsonResponse<T> jsonMeta(JsonMeta meta) {
        this.meta = meta
        return this
    }

    JsonResponse<T> jsonMeta(JsonError error) {
        this.error = error
        return this
    }
}
