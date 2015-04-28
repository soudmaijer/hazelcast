package com.oudmaijer.hazelcast;

public class Order {
    private Long id;
    private long version;

    public Order(Long id, long version) {
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}