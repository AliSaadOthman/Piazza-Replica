package org.piazza.repository.graph_repository;

import com.arangodb.ArangoCursor;
import org.piazza.repository.query.ConditionSeparator;
import org.piazza.repository.query.EdgeDirection;
import org.piazza.repository.query.FilterProperty;
import org.piazza.repository.query.SortDirection;
import org.springframework.lang.Nullable;

import java.util.Collection;

public interface EdgeBaseRepository {


    <T> ArangoCursor<T> findAll1(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection,String filterVariable,int min,int max);

    <T> ArangoCursor<T> findBy(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection, Collection<FilterProperty> edgeFilters, Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator, ConditionSeparator vertexConditionSeparator, String filterVariable,int min,int max);
    <T> ArangoCursor<T> findBySort(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection,Collection<FilterProperty> edgeFilters,Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator,ConditionSeparator vertexConditionSeparator, String sortField, SortDirection sortDirection,String sortVariable,String filterVariable,int min,int max);
    <T> ArangoCursor<T> findByLimit(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection, Collection<FilterProperty> edgeFilters,Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator,ConditionSeparator vertexConditionSeparator, Integer limit,String filterVariable,int min,int max);
    <T> ArangoCursor<T> findBySortLimit(Class<T> entityClass, String nodeId, String graphName, EdgeDirection edgeDirection, Collection<FilterProperty> edgeFilters,Collection<FilterProperty> vertexFilters, ConditionSeparator edgeConditionSeparator,ConditionSeparator vertexConditionSeparator, String sortField, SortDirection sortDirection,String sortVariable, Integer limit,String filterVariable,int min,int max)  ;

}


