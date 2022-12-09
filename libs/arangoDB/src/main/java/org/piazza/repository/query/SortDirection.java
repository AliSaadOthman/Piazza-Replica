package org.piazza.repository.query;

public enum SortDirection {

    DESC("DESC"),
    ASC("ASC");

    private final String sortDirection;

    private SortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortDirection() {
        return this.sortDirection;
    }

}
