package org.piazza.repository.query;

public class FilterProperty {

    private String key;
    private Object value;
    private FilterCondition filterCondition;

    public FilterProperty(String key, Object value, FilterCondition filterCondition) {
        this.key = key;
        this.value = value;
        this.filterCondition = filterCondition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFilterCondition() {
        return filterCondition.getCondition();
    }

    public void setFilterCondition(FilterCondition filterCondition) {
        this.filterCondition = filterCondition;
    }
}
