package org.piazza.repository.query;

import java.util.HashMap;
import java.util.Map;

public class ArangodbGraphQuery extends ArangodbQueryBuilder {

    HashMap<String, Object> bindVariables;
    String edgeDirection;

    public ArangodbGraphQuery(Map<String, String> filterVariables, String graphName, String nodeId, EdgeDirection edgeDirection) {
        super(null, filterVariables);
        this.bindVariables = new HashMap<String, Object>();
        this.bindVariables.put("graphName", graphName);
        this.edgeDirection=edgeDirection.getDirection();
        this.bindVariables.put("nodeId", nodeId);
    }

    @Override
    public ArangodbGraphQuery Builder() {
        queryBuilder.append("FOR ")
                .append(filterVariables.get("v"))
                .append(',')
                .append(filterVariables.get("e"))
                .append(" IN ");

        return this;
    }

    public ArangodbQueryBuilder setGraphCondition(HashMap<String, Object> bindVariables,int min ,int max) {
        bindVariables.putAll(this.bindVariables);
        queryBuilder.append(min)
                .append("..")
                .append(max)
                .append(" ")
                .append(edgeDirection)
                .append(" ")
                .append("@nodeId")
                .append(" ")
                .append("@graphName ")
                .append("\n");
        return this;
    }

}
