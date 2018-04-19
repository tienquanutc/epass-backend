package mongodb;

import com.mongodb.async.SingleResultCallback;

import java.util.concurrent.CompletableFuture;

/**
 * Created by chipn@eway.vn on 2/9/17.
 */
public class MongoCompletableFuture<T> extends CompletableFuture<T> implements SingleResultCallback<T> {

    @Override
    public void onResult(T result, Throwable throwable) {
        if (throwable != null) {
            this.completeExceptionally(throwable);
            return;
        }

        try {
            this.complete(result);
        } catch (Exception ex) {
            this.completeExceptionally(ex);
        }
    }

}