package org.piazza.repository.graph_repository;

import com.arangodb.ArangoCursor;
import com.arangodb.springframework.core.template.ArangoTemplate;
import org.piazza.repository.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

@Repository
public class EdgeBaseRepositoryImpl implements EdgeBaseRepository {
    @Autowired
    private ArangoTemplate arangoTemplate;

    @Override
    public <T> ArangoCursor<T> findAll1(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection,String filterVariable,int min,int max) {
        HashMap<String, Object> bindVars = new HashMap<String, Object>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("v", "v");
        filterVariables.put("e", "e");
        ArangodbGraphQuery arangodbCollectionQueryBuilder = new ArangodbGraphQuery(filterVariables, graphName, nodeId, edgeDirection);

        String query = arangodbCollectionQueryBuilder
                .Builder()
                .setGraphCondition(bindVars,min,max)
                .addReturn(filterVariables.get(filterVariable))
                .build();
        return arangoTemplate.query(query, bindVars, entityClass);


    }

    @Override
    public <T> ArangoCursor<T> findBy(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection, Collection<FilterProperty> edgeFilters, Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator, ConditionSeparator vertexConditionSeparator, String filterVariable,int min,int max) {
        HashMap<String, Object> bindVars = new HashMap<String, Object>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("v", "v");
        filterVariables.put("e", "e");
        ArangodbGraphQuery arangodbCollectionQueryBuilder = new ArangodbGraphQuery(filterVariables, graphName, nodeId, edgeDirection);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .setGraphCondition(bindVars,min,max)
                .addFilter(filterVariables.get("v"), vertexFilters, vertexConditionSeparator, bindVars)
                .addFilter(filterVariables.get("e"), edgeFilters, edgeConditionSeparator, bindVars)
                .addReturn(filterVariables.get(filterVariable))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findBySort(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection, Collection<FilterProperty> edgeFilters, Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator, ConditionSeparator vertexConditionSeparator, String sortField, SortDirection sortDirection, String sortVariable,String filterVariable,int min ,int max) {

        HashMap<String, Object> bindVars = new HashMap<String, Object>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("v", "v");
        filterVariables.put("e", "e");
        ArangodbGraphQuery arangodbCollectionQueryBuilder = new ArangodbGraphQuery(filterVariables, graphName, nodeId, edgeDirection);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .setGraphCondition(bindVars,min,max)
                .addFilter(filterVariables.get("v"), vertexFilters, vertexConditionSeparator, bindVars)
                .addFilter(filterVariables.get("e"), edgeFilters, edgeConditionSeparator, bindVars)
                .addSort(filterVariables.get(sortVariable), sortField, sortDirection)
                .addReturn(filterVariables.get(filterVariable))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findByLimit(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection,  Collection<FilterProperty> edgeFilters, Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator, ConditionSeparator vertexConditionSeparator, Integer limit,String filterVariable,int min,int max) {
        HashMap<String, Object> bindVars = new HashMap<String, Object>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("v", "v");
        filterVariables.put("e", "e");
        ArangodbGraphQuery arangodbCollectionQueryBuilder = new ArangodbGraphQuery(filterVariables, graphName, nodeId, edgeDirection);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .setGraphCondition(bindVars,min,max)
                .addFilter(filterVariables.get("v"), vertexFilters, vertexConditionSeparator, bindVars)
                .addFilter(filterVariables.get("e"), edgeFilters, edgeConditionSeparator, bindVars)
                .addLimit(limit)
                .addReturn(filterVariables.get(filterVariable))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findBySortLimit(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection,Collection<FilterProperty> edgeFilters, Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator, ConditionSeparator vertexConditionSeparator, String sortField, SortDirection sortDirection, String sortVariable, Integer limit,String filterVariable,int min ,int max) {
        HashMap<String, Object> bindVars = new HashMap<String, Object>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("v", "v");
        filterVariables.put("e", "e");
        ArangodbGraphQuery arangodbCollectionQueryBuilder = new ArangodbGraphQuery(filterVariables, graphName, nodeId, edgeDirection);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .setGraphCondition(bindVars,min,max)
                .addFilter(filterVariables.get("v"), vertexFilters, vertexConditionSeparator, bindVars)
                .addFilter(filterVariables.get("e"), edgeFilters, edgeConditionSeparator, bindVars)
                .addSort(filterVariables.get(sortVariable), sortField, sortDirection)
                .addLimit(limit)
                .addReturn(filterVariables.get(filterVariable))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    public void shutDown() {
        if (arangoTemplate != null)
            arangoTemplate.driver().shutdown();
    }
}



