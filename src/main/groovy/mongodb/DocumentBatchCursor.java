package mongodb;

import com.mongodb.async.AsyncBatchCursor;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by chipn@eway.vn on 2/9/17.
 */
public class DocumentBatchCursor {

    private final AsyncBatchCursor<Document> delegate;

    public DocumentBatchCursor(AsyncBatchCursor<Document> delegate) {
        this.delegate = delegate;
    }

    public CompletableFuture<List<Document>> next() {
        MongoCompletableFuture<List<Document>> future = new MongoCompletableFuture<>();
        delegate.next(future);
        return future;
    }

    public void setBatchSize(int batchSize) {
        delegate.setBatchSize(batchSize);
    }

    public int getBatchSize() {
        return delegate.getBatchSize();
    }

    public boolean isClosed() {
        return delegate.isClosed();
    }

    public void close() {
        delegate.close();
    }
}