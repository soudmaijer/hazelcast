package com.oudmaijer.hazelcast;

import org.springframework.jms.annotation.JmsListener;

import javax.inject.Inject;
import javax.inject.Named;

@Named("orderListenerDistributedLockDemo")
public class OrderListenerBrokenDemo {

    private final OrderRepository orderRepository;

    @Inject
    public OrderListenerBrokenDemo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @JmsListener(destination = "incomingOrderQueue")
    public void processIncomingOrder(Order order) {
        if (orderRepository.exists(order.getId())) {
            orderRepository.update(order);
        } else {
            orderRepository.persist(order);
        }
    }
}

