package org.piazza.repository.query;

import java.util.Map;

public class ArangodbCollectionQueryBuilder extends ArangodbQueryBuilder{
    public ArangodbCollectionQueryBuilder(String collectionName, Map<String, String> filterVariables) {
        super(collectionName, filterVariables);
    }

    @Override
    public ArangodbQueryBuilder Builder() {
        queryBuilder.append("FOR ");
        for (String filterVariable: filterVariables.values()) {
            queryBuilder.append(filterVariable).append(", ");
        }
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(" IN ").append(collectionName).append("\n");
        return this;
    }

}
