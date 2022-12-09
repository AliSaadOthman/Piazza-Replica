package org.piazza.repository.query;

public enum EdgeDirection {
    OUTBOUND("OUTBOUND"),
    INBOUND("INBOUND"),

    ANY("ANY");


    private final String direction;

    private EdgeDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }
}
