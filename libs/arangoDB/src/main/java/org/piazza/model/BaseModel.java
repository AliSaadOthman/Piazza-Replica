package org.piazza.model;

import com.arangodb.springframework.annotation.ArangoId;
import org.springframework.data.annotation.Id;

public class BaseModel {

    @Id
    private String id;

    @ArangoId
    private String arangoId;

    public String getId() {
        return id;
    }

    public String getArangoId() {
        return arangoId;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", arangoId='" + arangoId + '\'' +
                '}';
    }
}
