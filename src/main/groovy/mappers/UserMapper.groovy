package mappers

import model.User
import org.bson.Document
import org.bson.types.ObjectId
import utils.ModelMapper

class UserMapper extends ModelMapper<User> {
    @Override
    User toModel(Document document) {
        if (document == null)
            return null
        return new User()
                .databaseId(document._id as String)
                .username(document.username as String)
                .hashPassword(document.hash_password as String)
                .salt(document.salt as String)
                .createdAt(document.created_at as Date)
                .updatedAt(document.updated_at as Date)
    }

    @Override
    Document toDocument(User model) {
        if (model == null)
            return null
        Document doc = new Document()

        if (model.databaseId) {
            doc._id = new ObjectId(model.databaseId)
        }

        doc.putAll([
                username     : model.username,
                hash_password: model.hashPassword,
                salt         : model.salt,
                created_at   : model.createdAt,
                updated_at   : model.updatedAt
        ])

        doc.values().removeIf(Objects.&isNull)
        return doc;
    }
}
