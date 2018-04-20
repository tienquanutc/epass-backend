package mongodb;

import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.FindIterable;
import com.mongodb.client.model.Collation;
import org.bson.Document;
import org.bson.conversions.Bson;
import utils.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by chipn@eway.vn on 2/9/17.
 */
public class ModelFindIterable<T> {

    private FindIterable<Document> delegate;
    private final ModelMapper<T> mapper;

    public ModelFindIterable(DocumentFindIterable findIterable, ModelMapper<T> mapper) {
        this(findIterable.delegate, mapper);
    }

    public ModelFindIterable(FindIterable<Document> delegate, ModelMapper<T> mapper) {
        this.delegate = delegate;
        this.mapper = mapper;
    }

    public ModelFindIterable filter(Bson filter) {
        delegate.filter(filter);
        return this;
    }

    public ModelFindIterable limit(int limit) {
        delegate.limit(limit);
        return this;
    }

    public ModelFindIterable skip(int skip) {
        delegate.skip(skip);
        return this;
    }

    public ModelFindIterable maxTime(long maxTime, TimeUnit timeUnit) {
        delegate.maxTime(maxTime, timeUnit);
        return this;
    }

    public ModelFindIterable maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        delegate.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }

    public ModelFindIterable modifiers(Bson modifiers) {
        delegate.modifiers(modifiers);
        return this;
    }

    public ModelFindIterable projection(Bson projection) {
        delegate.projection(projection);
        return this;
    }

    public ModelFindIterable sort(Bson sort) {
        delegate.sort(sort);
        return this;
    }

    public ModelFindIterable noCursorTimeout(boolean noCursorTimeout) {
        delegate.noCursorTimeout(noCursorTimeout);
        return this;
    }

    public ModelFindIterable oplogReplay(boolean oplogReplay) {
        delegate.oplogReplay(oplogReplay);
        return this;
    }

    public ModelFindIterable partial(boolean partial) {
        delegate.partial(partial);
        return this;
    }

    public ModelFindIterable cursorType(CursorType cursorType) {
        delegate.cursorType(cursorType);
        return this;
    }

    public CompletableFuture<Document> firstDocument() {
        MongoCompletableFuture<Document> future = new MongoCompletableFuture<>();
        delegate.first(future);
        return future;
    }

    public CompletableFuture<T> first() {
        return this.firstDocument().thenApply(mapper::toModel);
    }

    public CompletableFuture<List<Document>> into() {
        MongoCompletableFuture<List<Document>> future = new MongoCompletableFuture<>();
        delegate.into(new ArrayList<>(), future);
        return future;
    }

    public CompletableFuture<List<T>> intoModels() {
        return this.into().thenApply(mapper::toModels);
    }

    public void forEach(Block<Document> block, SingleResultCallback<Void> callback) {
        delegate.forEach(block, callback);
    }

    public void forEachModels(Block<T> block, SingleResultCallback<Void> callback) {
        this.forEach(document -> block.apply(mapper.toModel(document)), callback);
    }

    public ModelFindIterable batchSize(int batchSize) {
        delegate.batchSize(batchSize);
        return this;
    }

    public CompletableFuture<ModelBatchCursor> batchCursor() {
        MongoCompletableFuture<AsyncBatchCursor<Document>> future = new MongoCompletableFuture<>();
        delegate.batchCursor(future);
        return future.thenApply(it -> new ModelBatchCursor(it, mapper));
    }

    public ModelFindIterable collation(Collation collation) {
        delegate.collation(collation);
        return this;
    }
}