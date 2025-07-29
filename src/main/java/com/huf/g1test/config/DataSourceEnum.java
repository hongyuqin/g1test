package com.huf.g1test.config;

public enum DataSourceEnum {
    MASTER("master"),
    SLAVE("slave");

    private final String value;

    DataSourceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 