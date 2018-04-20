package utils;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chipn@eway.vn on 2/7/17.
 */
public abstract class ModelMapper<T> {

    public abstract T toModel(Document document);

    public abstract Document toDocument(T model);

    public final List<Document> toDocuments(List<T> models) {
        if (models == null) {
            return new ArrayList<>(0);
        }
        return models.stream().map(this::toDocument).collect(Collectors.toList());
    }

    public final List<T> toModels(List<Document> documents) {
        if (documents == null) {
            return new ArrayList<>(0);
        }
        return documents.stream().map(this::toModel).collect(Collectors.toList());
    }
}
