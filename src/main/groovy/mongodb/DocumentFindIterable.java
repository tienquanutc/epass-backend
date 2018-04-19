package mongodb;

import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.FindIterable;
import com.mongodb.client.model.Collation;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by chipn@eway.vn on 2/9/17.
 */
public class DocumentFindIterable {

    final FindIterable<Document> delegate;

    public DocumentFindIterable(FindIterable delegate) {
        this.delegate = delegate;
    }

    public DocumentFindIterable filter(Bson filter) {
        delegate.filter(filter);
        return this;
    }

    public DocumentFindIterable limit(int limit) {
        delegate.limit(limit);
        return this;
    }

    public DocumentFindIterable skip(int skip) {
        delegate.skip(skip);
        return this;
    }

    public DocumentFindIterable maxTime(long maxTime, TimeUnit timeUnit) {
        delegate.maxTime(maxTime, timeUnit);
        return this;
    }

    public DocumentFindIterable maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        delegate.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }

    public DocumentFindIterable modifiers(Bson modifiers) {
        delegate.modifiers(modifiers);
        return this;
    }

    public DocumentFindIterable projection(Bson projection) {
        delegate.projection(projection);
        return this;
    }

    public DocumentFindIterable sort(Bson sort) {
        delegate.sort(sort);
        return this;
    }

    public DocumentFindIterable noCursorTimeout(boolean noCursorTimeout) {
        delegate.noCursorTimeout(noCursorTimeout);
        return this;
    }

    public DocumentFindIterable oplogReplay(boolean oplogReplay) {
        delegate.oplogReplay(oplogReplay);
        return this;
    }

    public DocumentFindIterable partial(boolean partial) {
        delegate.partial(partial);
        return this;
    }

    public DocumentFindIterable cursorType(CursorType cursorType) {
        delegate.cursorType(cursorType);
        return this;
    }

    public CompletableFuture<Document> first() {
        MongoCompletableFuture<Document> future = new MongoCompletableFuture<>();
        delegate.first(future);
        return future;
    }

    public CompletableFuture<List<Document>> intoDocuments() {
        MongoCompletableFuture<List<Document>> future = new MongoCompletableFuture<>();
        delegate.into(new ArrayList<Document>(), future);
        return future;
    }

    public void forEach(Block<Document> block, SingleResultCallback<Void> callback) {
        delegate.forEach(block, callback);
    }

    public DocumentFindIterable batchSize(int batchSize) {
        delegate.batchSize(batchSize);
        return this;
    }

    public CompletableFuture<DocumentBatchCursor> batchCursor() {
        MongoCompletableFuture<AsyncBatchCursor<Document>> future = new MongoCompletableFuture<>();
        delegate.batchCursor(future);
        return future.thenApply(asyncBatchCursor -> new DocumentBatchCursor(asyncBatchCursor));
    }

    public DocumentFindIterable collation(Collation collation) {
        delegate.collation(collation);
        return this;
    }
}