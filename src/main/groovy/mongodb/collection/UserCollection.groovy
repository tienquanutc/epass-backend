package mongodb.collection

import groovy.transform.InheritConstructors
import mappers.UserMapper
import model.User
import mongodb.DocumentCollection
import mongodb.ModelCollection
import org.apache.commons.lang3.Validate
import org.bson.Document
import org.bson.types.ObjectId
import utils.ModelMapper

class UserCollection extends ModelCollection<User> {
    UserCollection(String uri) {
        super(uri, UserMapper.class)
    }

    User insert(User user) {
        def now = new Date()
        user.createdAt(now).updatedAt(now)
        return this.insertOneModel(user).join()
    }

    boolean isExist(String username) {
        this.find([username: username] as Document).projection([_id: 1] as Document).intoDocuments().join()
    }

    User changePassword(String databaseId, String oldPassword, String newPassword) {
        def id = new ObjectId(databaseId)
        User user = this.findModel(id).first().join()
        if (user.hashPassword != oldPassword)
            throw new RuntimeException("old password incorrect")
        user.hashPassword(newPassword).salt(newPassword)
        return this.update(user)
    }

    User update(User model) {
        def objectId = new ObjectId(model.databaseId)
        return update(objectId, model)
    }

    User update(String id, User model) {
        def objectId = new ObjectId(id)
        return update(objectId, model)
    }

    User update(ObjectId id, User model) {
        def currentDate = new Date()
        model.updatedAt(currentDate)

        def document = this.mapper.toDocument(model)

        def filter = ["_id": id] as Document
        def update = [
                '$set': document
        ] as Document


        return this.findOneAndUpdateModel(filter, update).join()
    }
}
