package org.piazza.repository.collection_repository;

import com.arangodb.ArangoCursor;
import com.arangodb.DbName;
import com.arangodb.springframework.core.template.ArangoTemplate;
import org.piazza.repository.query.ArangodbCollectionQueryBuilder;
import org.piazza.repository.query.ConditionSeparator;
import org.piazza.repository.query.FilterProperty;
import org.piazza.repository.query.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

@Repository
public class BaseRepositoryImpl implements BaseRepository {

    @Autowired
    private ArangoTemplate arangoTemplate;

    @Override
    public <T> ArangoCursor<T> findBy(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator) {
        createNotExist(collectionName);
        Map<String, Object> bindVars = new Hashtable<>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("obj", "obj");
        ArangodbCollectionQueryBuilder arangodbCollectionQueryBuilder = new ArangodbCollectionQueryBuilder(collectionName, filterVariables);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .addFilter(filterVariables.get("obj"), filters, conditionSeparator, bindVars)
                .addReturn(filterVariables.get("obj"))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findBySort(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, String sortField, SortDirection sortDirection) {
        createNotExist(collectionName);
        Map<String, Object> bindVars = new Hashtable<>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("obj", "obj");
        ArangodbCollectionQueryBuilder arangodbCollectionQueryBuilder = new ArangodbCollectionQueryBuilder(collectionName, filterVariables);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .addFilter(filterVariables.get("obj"), filters, conditionSeparator, bindVars)
                .addSort(filterVariables.get("obj"), sortField, sortDirection)
                .addReturn(filterVariables.get("obj"))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findByLimit(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, Integer limit) {
        createNotExist(collectionName);
        Map<String, Object> bindVars = new Hashtable<>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("obj", "obj");
        ArangodbCollectionQueryBuilder arangodbCollectionQueryBuilder = new ArangodbCollectionQueryBuilder(collectionName, filterVariables);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .addFilter(filterVariables.get("obj"), filters, conditionSeparator, bindVars)
                .addLimit(limit)
                .addReturn(filterVariables.get("obj"))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    @Override
    public <T> ArangoCursor<T> findBySortLimit(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, String sortField, SortDirection sortDirection, Integer limit) {
        createNotExist(collectionName);
        Map<String, Object> bindVars = new Hashtable<>();
        Hashtable<String, String> filterVariables = new Hashtable<>();
        filterVariables.put("obj", "obj");
        ArangodbCollectionQueryBuilder arangodbCollectionQueryBuilder = new ArangodbCollectionQueryBuilder(collectionName, filterVariables);
        String query = arangodbCollectionQueryBuilder
                .Builder()
                .addFilter(filterVariables.get("obj"), filters, conditionSeparator, bindVars)
                .addSort(filterVariables.get("obj"), sortField, sortDirection)
                .addLimit(limit)
                .addReturn(filterVariables.get("obj"))
                .build();

        return arangoTemplate.query(query, bindVars, entityClass);
    }

    public void shutDown() {
        if (arangoTemplate != null)
            arangoTemplate.driver().shutdown();
    }

    public void createNotExist(String collectionName){
        try {
            arangoTemplate.driver().db(DbName.of("piazza")).createCollection(collectionName);
        }catch (Exception e){
            if(!e.getMessage().contains("duplicate name")){
                throw e;
            }
        }


    }
}
