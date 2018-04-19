package mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoNamespace
import com.mongodb.async.client.MongoClient
import com.mongodb.async.client.MongoClients
import com.mongodb.async.client.MongoDatabase
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.model.*
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import groovy.transform.CompileStatic
import org.apache.commons.lang3.Validate
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

import java.util.concurrent.CompletableFuture

/**
 * For MongoDB Async Driver
 * Created by chipn@eway.vn on 2/7/17.
 */
@CompileStatic
class DocumentCollection {

    protected final com.mongodb.async.client.MongoCollection<Document> delegate

    /**
     * URI Example: mongodb://username:password@host:port/database.collection
     * @param uri
     */
    DocumentCollection(String uri) {
        Validate.notBlank(uri, "uri must be not blank")

        ConnectionString connectionString = new ConnectionString(uri)
        Validate.notBlank(connectionString.database, "database name must be not blank in URI")
        Validate.notBlank(connectionString.collection, "collection name must be not blank in URI")

        MongoClient mongoClient = MongoClients.create(uri)
        MongoDatabase database = mongoClient.getDatabase(connectionString.database)
        this.delegate = database.getCollection(connectionString.collection)
    }

    CompletableFuture<Document> get(ObjectId id) {
        return find(id).first()
    }

    CompletableFuture<List<Document>> get(List<ObjectId> ids) {
        return find(ids).intoDocuments()
    }

    DocumentFindIterable find() {
        return new DocumentFindIterable(delegate.find())
    }

    DocumentFindIterable find(ObjectId id) {
        Validate.notNull(id, "id must not be null")
        return find([_id: id] as Document)
    }

    DocumentFindIterable find(List<ObjectId> ids) {
        Validate.notEmpty(ids, "ids must not be empty")
        return find([_id: [$in: ids]] as Document)
    }

    DocumentFindIterable find(Bson filter) {
        return new DocumentFindIterable(delegate.find(filter))
    }

    CompletableFuture<Long> count() {
        def future = new MongoCompletableFuture<>()
        delegate.count(future)
        return future
    }

    CompletableFuture<Long> count(Bson filter) {
        def future = new MongoCompletableFuture<>()
        delegate.count(filter, future)
        return future
    }

    CompletableFuture<Long> count(Bson filter, CountOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.count(filter, options, future)
        return future
    }

    CompletableFuture<BulkWriteResult> bulkWrite(List<? extends WriteModel<? extends Document>> requests) {
        def future = new MongoCompletableFuture<>()
        delegate.bulkWrite(requests, future)
        return future
    }

    CompletableFuture<BulkWriteResult> bulkWrite(List<? extends WriteModel<? extends Document>> requests, BulkWriteOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.bulkWrite(requests, options, future)
        return future
    }

    CompletableFuture<Document> insertOne(Document document) {
        def future = new MongoCompletableFuture<>()
        delegate.insertOne(document, { Void aVoid, Throwable throwable -> future.onResult(document, throwable) })
        return future
    }

    CompletableFuture<Document> insertOne(Document document, InsertOneOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.insertOne(document, options, { Void aVoid, Throwable throwable -> future.onResult(document, throwable) })
        return future
    }

    CompletableFuture<Document> insertMany(List<? extends Document> documents) {
        def future = new MongoCompletableFuture<>()
        delegate.insertMany(documents, { Void aVoid, Throwable throwable -> future.onResult(documents, throwable) })
        return future

    }

    CompletableFuture<Document> insertMany(List<? extends Document> documents, InsertManyOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.insertMany(documents, options, { Void aVoid, Throwable throwable -> future.onResult(documents, throwable) })
        return future
    }

    CompletableFuture<DeleteResult> deleteOne(ObjectId id) {
        Validate.notNull(id, "id must not be null")
        return deleteOne([_id: id] as Document)
    }

    CompletableFuture<DeleteResult> deleteOne(ObjectId id, DeleteOptions options) {
        Validate.notNull(id, "id must not be null")
        return deleteOne([_id: id] as Document, options)
    }

    CompletableFuture<DeleteResult> deleteOne(Bson filter) {
        def future = new MongoCompletableFuture<>()
        delegate.deleteOne(filter, future)
        return future
    }

    CompletableFuture<DeleteResult> deleteOne(Bson filter, DeleteOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.deleteOne(filter, options, future)
        return future
    }

    CompletableFuture<DeleteResult> deleteMany(List<ObjectId> ids) {
        Validate.notEmpty(ids, "ids must not be empty")
        return deleteMany([_id: [$in: ids]] as Document)
    }

    CompletableFuture<DeleteResult> deleteMany(List<ObjectId> ids, DeleteOptions options) {
        Validate.notEmpty(ids, "ids must not be empty")
        return deleteMany([_id: [$in: ids]] as Document, options)
    }

    CompletableFuture<DeleteResult> deleteMany(Bson filter) {
        def future = new MongoCompletableFuture<>()
        delegate.deleteMany(filter, future)
        return future
    }

    CompletableFuture<DeleteResult> deleteMany(Bson filter, DeleteOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.deleteMany(filter, options, future)
        return future
    }

    CompletableFuture<UpdateResult> replaceOne(ObjectId id, Document replacement) {
        Validate.notNull(id, "id must not be null")
        return replaceOne([_id: id] as Document, replacement)
    }

    CompletableFuture<UpdateResult> replaceOne(ObjectId id, Document replacement, UpdateOptions options) {
        Validate.notNull(id, "id must not be null")
        return replaceOne([_id: id] as Document, replacement, options)
    }

    CompletableFuture<UpdateResult> replaceOne(Bson filter, Document replacement) {
        def future = new MongoCompletableFuture<>()
        delegate.replaceOne(filter, replacement, future)
        return future
    }

    CompletableFuture<UpdateResult> replaceOne(Bson filter, Document replacement, UpdateOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.replaceOne(filter, replacement, options, future)
        return future
    }

    CompletableFuture<UpdateResult> updateOne(ObjectId id, Bson update) {
        Validate.notNull(id, "id must not be null")
        return updateOne([_id: id] as Document, update)
    }

    CompletableFuture<UpdateResult> updateOne(ObjectId id, Bson update, UpdateOptions options) {
        Validate.notNull(id, "id must not be null")
        return updateOne([_id: id] as Document, update, options)
    }

    CompletableFuture<UpdateResult> updateOne(Bson filter, Bson update) {
        def future = new MongoCompletableFuture<>()
        delegate.updateOne(filter, update, future)
        return future
    }

    CompletableFuture<UpdateResult> updateOne(Bson filter, Bson update, UpdateOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.updateOne(filter, update, options, future)
        return future
    }

    CompletableFuture<UpdateResult> updateMany(List<ObjectId> ids, Bson update) {
        Validate.notEmpty(ids, "ids must not be empty")
        return updateMany([_id: [$in: ids]] as Document, update)
    }

    CompletableFuture<UpdateResult> updateMany(List<ObjectId> ids, Bson update, UpdateOptions options) {
        Validate.notEmpty(ids, "ids must not be empty")
        return updateMany([_id: [$in: ids]] as Document, update, options)
    }

    CompletableFuture<UpdateResult> updateMany(Bson filter, Bson update) {
        def future = new MongoCompletableFuture<>()
        delegate.updateMany(filter, update, future)
        return future
    }

    CompletableFuture<UpdateResult> updateMany(Bson filter, Bson update, UpdateOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.updateMany(filter, update, options, future)
        return future
    }

    CompletableFuture<Document> findOneAndDelete(ObjectId id) {
        Validate.notNull(id, "id must not be null")
        return findOneAndDelete([_id: id] as Document)
    }

    CompletableFuture<Document> findOneAndDelete(ObjectId id, FindOneAndDeleteOptions options) {
        Validate.notNull(id, "id must not be null")
        return findOneAndDelete([_id: id] as Document, options)
    }

    CompletableFuture<Document> findOneAndDelete(Bson filter) {
        def future = new MongoCompletableFuture<>()
        delegate.findOneAndDelete(filter, future)
        return future
    }

    CompletableFuture<Document> findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.findOneAndDelete(filter, options, future)
        return future
    }

    CompletableFuture<Document> findOneAndReplace(ObjectId id, Document replacement) {
        Validate.notNull(id, "id must not be null")
        return findOneAndReplace([_id: id] as Document, replacement)
    }

    CompletableFuture<Document> findOneAndReplace(ObjectId id, Document replacement, FindOneAndReplaceOptions options) {
        Validate.notNull(id, "id must not be null")
        return findOneAndReplace([_id: id] as Document, replacement, options)
    }

    CompletableFuture<Document> findOneAndReplace(Bson filter, Document replacement) {
        def future = new MongoCompletableFuture<>()
        def findOneAndReplaceOptions = new FindOneAndReplaceOptions(returnDocument: ReturnDocument.AFTER)
        delegate.findOneAndReplace(filter, replacement, findOneAndReplaceOptions, future)
        return future
    }

    CompletableFuture<Document> findOneAndReplace(Bson filter, Document replacement, FindOneAndReplaceOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.findOneAndReplace(filter, replacement, options, future)
        return future
    }

    CompletableFuture<Document> findOneAndUpdate(ObjectId id, Bson update) {
        Validate.notNull(id, "id must not be null")
        return findOneAndUpdate([_id: id] as Document, update)
    }

    CompletableFuture<Document> findOneAndUpdate(ObjectId id, Bson update, FindOneAndUpdateOptions options) {
        Validate.notNull(id, "id must not be null")
        return findOneAndUpdate([_id: id] as Document, update, options)
    }

    CompletableFuture<Document> findOneAndUpdate(Bson filter, Bson update) {
        def future = new MongoCompletableFuture<>()
        def findOneAndUpdateOptions = new FindOneAndUpdateOptions(returnDocument: ReturnDocument.AFTER)
        delegate.findOneAndUpdate(filter, update, findOneAndUpdateOptions, future)
        return future
    }

    CompletableFuture<Document> findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.findOneAndUpdate(filter, update, options, future)
        return future
    }

    CompletableFuture<Void> drop() {
        def future = new MongoCompletableFuture<>()
        delegate.drop(future)
        return future
    }

    CompletableFuture<String> createIndex(Bson key) {
        def future = new MongoCompletableFuture<>()
        delegate.createIndex(key, future)
        return future
    }

    CompletableFuture<String> createIndex(Bson key, IndexOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.createIndex(key, options, future)
        return future
    }

    CompletableFuture<List<String>> createIndexes(List<IndexModel> indexes) {
        def future = new MongoCompletableFuture<>()
        delegate.createIndexes(indexes, future)
        return future
    }

    CompletableFuture<Void> dropIndex(String indexName) {
        def future = new MongoCompletableFuture<>()
        delegate.dropIndex(indexName, future)
        return future
    }

    CompletableFuture<Void> dropIndex(Bson keys) {
        def future = new MongoCompletableFuture<>()
        delegate.dropIndex(keys, future)
        return future
    }

    CompletableFuture<Void> dropIndexes() {
        def future = new MongoCompletableFuture<>()
        delegate.dropIndexes(future)
        return future
    }

    CompletableFuture<Void> renameCollection(MongoNamespace newCollectionNamespace) {
        def future = new MongoCompletableFuture<>()
        delegate.renameCollection(newCollectionNamespace, future)
        return future
    }

    CompletableFuture<Void> renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
        def future = new MongoCompletableFuture<>()
        delegate.renameCollection(newCollectionNamespace, options, future)
        return future
    }

}

