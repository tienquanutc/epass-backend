package mongodb

import com.mongodb.client.model.*
import groovy.transform.CompileStatic
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import utils.ModelMapper

import java.util.concurrent.CompletableFuture

/**
 * For MongoDB Async Driver
 * Created by chipn@eway.vn on 2/7/17.
 */
@CompileStatic
class ModelCollection<T> extends DocumentCollection {

    final ModelMapper<T> mapper

    ModelCollection(String uri, Class<? extends ModelMapper<T>> mapperClass) {
        this(uri, mapperClass.newInstance())
    }

    ModelCollection(String uri, ModelMapper<T> mapper) {
        super(uri)
        this.mapper = mapper
    }

    CompletableFuture<T> getModel(ObjectId id) {
        return findModel(id).first()
    }

    CompletableFuture<List<T>> getModels(List<ObjectId> ids) {
        return findModels(ids).intoModels()
    }

    ModelFindIterable<T> findModel(ObjectId id) {
        return new ModelFindIterable<T>(find(id), mapper)
    }

    ModelFindIterable<T> findModels(List<ObjectId> ids) {
        return new ModelFindIterable<T>(find(ids), mapper)
    }

    ModelFindIterable<T> findModels() {
        return new ModelFindIterable(find(), mapper)
    }

    ModelFindIterable<T> findModels(Bson filter) {
        return new ModelFindIterable(find(filter), mapper)
    }

    CompletableFuture<T> findOneAndUpdateModel(ObjectId id, Bson update) {
        return findOneAndUpdate(id, update).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndUpdateModel(ObjectId id, Bson update, FindOneAndUpdateOptions options) {
        return findOneAndUpdate(id, update, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndUpdateModel(Bson filter, Bson update) {
        return findOneAndUpdate(filter, update).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndUpdateModel(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return findOneAndUpdate(filter, update, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndReplaceModel(ObjectId id, Document replacement) {
        return findOneAndReplace(id, replacement).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndReplaceModel(ObjectId id, Document replacement, FindOneAndReplaceOptions options) {
        return findOneAndReplace(id, replacement, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndReplaceModel(Bson filter, Document replacement) {
        return findOneAndReplace(filter, replacement).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndReplaceModel(Bson filter, Document replacement, FindOneAndReplaceOptions options) {
        return findOneAndReplace(filter, replacement, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndDeleteModel(ObjectId id) {
        return findOneAndDelete(id).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndDeleteModel(ObjectId id, FindOneAndDeleteOptions options) {
        return findOneAndDelete(id, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndDeleteModel(Bson filter) {
        return findOneAndDelete(filter).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> findOneAndDeleteModel(Bson filter, FindOneAndDeleteOptions options) {
        return findOneAndDelete(filter, options).thenApply { mapper.toModel(it) }
    }

    CompletableFuture<T> insertOneModel(T model) {
        def document = mapper.toDocument(model)
        return insertOne(document).thenApply { mapper.toModel(document) }
    }

    CompletableFuture<T> insertOneModel(T model, InsertOneOptions options) {
        def document = mapper.toDocument(model)
        return insertOne(document).thenApply { mapper.toModel(document) }
    }

    CompletableFuture<List<T>> insertManyModels(List<T> models) {
        def documents = mapper.toDocuments(models)
        return insertMany(documents).thenApply { mapper.toModels(documents) }
    }

    CompletableFuture<List<T>> insertManyModels(List<T> models, InsertManyOptions options) {
        def documents = mapper.toDocuments(models)
        return insertMany(documents, options).thenApply { mapper.toModels(documents) }
    }

}

