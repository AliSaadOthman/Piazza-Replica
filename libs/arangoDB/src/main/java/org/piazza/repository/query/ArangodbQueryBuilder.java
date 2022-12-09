package org.piazza.repository.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class ArangodbQueryBuilder {

    final StringBuilder queryBuilder;
    final String collectionName;
    final Map<String, String> filterVariables;

    public ArangodbQueryBuilder(String collectionName, Map<String, String> filterVariables) {
        this.queryBuilder = new StringBuilder();
        this.filterVariables = filterVariables;
        this.collectionName = collectionName;
    }

    public abstract ArangodbQueryBuilder Builder();

    public ArangodbQueryBuilder addFilter(String filterVariable, Collection<FilterProperty> filterProperties, ConditionSeparator conditionSeparator, Map<String, Object> bindVars) {

        if(filterProperties.isEmpty()){
            return this;
        }

        queryBuilder.append("FILTER ( ");

        Iterator<FilterProperty> filterPropertyIterator = filterProperties.iterator();
        while (filterPropertyIterator.hasNext()) {
            FilterProperty filterProperty = filterPropertyIterator.next();
            bindVars.put(filterProperty.getKey(), filterProperty.getValue());

            switch (filterProperty.getFilterCondition()) {

                default -> queryBuilder
                        .append(String.format("%s.", filterVariable))
                        .append(String.format("%s ", filterProperty.getKey()))
                        .append(String.format("%s ", filterProperty.getFilterCondition()))
                        .append(String.format("@%s ", filterProperty.getKey()));
            }

            if (filterPropertyIterator.hasNext()) {
                queryBuilder.append(String.format(" %s ", conditionSeparator.getCondition()));
            }
        }
        queryBuilder.append(") ").append("\n");
        return this;
    }

    public ArangodbQueryBuilder addLimit(Integer limit) {
        queryBuilder.append("LIMIT ").append(limit.toString()).append("\n");
        return this;
    }

    public ArangodbQueryBuilder addSort(String filterVariable, String sortableField, SortDirection sortDirection) {
        queryBuilder
                .append("SORT ")
                .append(String.format("%s.", filterVariable))
                .append(String.format("%s ", sortableField))
                .append(sortDirection)
                .append("\n");
        return this;
    }

    public ArangodbQueryBuilder addReturn(String filterVariable) {
        queryBuilder.append("RETURN ").append(filterVariable).append("\n");
        return this;
    }

    public ArangodbQueryBuilder addConditionalSeparator(ConditionSeparator conditionSeparator) {
        queryBuilder.append(String.format(" %s ", conditionSeparator.getCondition()));
        return this;
    }

    public String build() {
        System.out.println(queryBuilder);
        return queryBuilder.toString();
    }
}