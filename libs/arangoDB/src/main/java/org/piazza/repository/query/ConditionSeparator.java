package org.piazza.repository.query;

public enum ConditionSeparator {

    AND("AND"),
    OR("OR");

    private final String condition;

    private ConditionSeparator(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return this.condition;
    }
}
