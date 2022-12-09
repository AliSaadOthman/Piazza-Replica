package org.piazza.repository.query;

public enum FilterCondition {

    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<="),
    IN("IN"),
    NOT_IN("NOT IN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE");

    private final String condition;

    private FilterCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return this.condition;
    }
}
