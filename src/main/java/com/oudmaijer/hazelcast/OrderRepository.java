package com.oudmaijer.hazelcast;

public interface OrderRepository {

    boolean exists(Long id);

    Order persist(Order order);

    void update(Order order);
}
