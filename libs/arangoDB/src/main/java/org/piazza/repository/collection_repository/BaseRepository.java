package org.piazza.repository.collection_repository;

import com.arangodb.ArangoCursor;
import org.piazza.repository.query.ConditionSeparator;
import org.piazza.repository.query.FilterProperty;
import org.piazza.repository.query.SortDirection;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BaseRepository {

    <T> ArangoCursor<T> findBy(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator);

    <T> ArangoCursor<T> findBySort(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, String sortField, SortDirection sortDirection);

    <T> ArangoCursor<T> findByLimit(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, Integer limit);

    <T> ArangoCursor<T> findBySortLimit(Class<T> entityClass, String collectionName, Collection<FilterProperty> filters, ConditionSeparator conditionSeparator, String sortField, SortDirection sortDirection, Integer limit);
}
