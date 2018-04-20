package mongodb;

import com.mongodb.async.AsyncBatchCursor;
import org.bson.Document;
import utils.ModelMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by chipn@eway.vn on 2/9/17.
 */
public class ModelBatchCursor<T> {

    private final AsyncBatchCursor<Document> delegate;
    private final ModelMapper<T> mapper;

    public ModelBatchCursor(AsyncBatchCursor<Document> delegate, ModelMapper<T> mapper) {
        this.delegate = delegate;
        this.mapper = mapper;
    }

    public CompletableFuture<List<Document>> next() {
        MongoCompletableFuture future = new MongoCompletableFuture<List<Document>>();
        delegate.next(future);
        return future;
    }

    public CompletableFuture<List<T>> nextModels() {
        return next().thenApply(documents -> mapper.toModels(documents));
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
