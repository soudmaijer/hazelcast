package com.oudmaijer.hazelcast;

import org.springframework.jms.annotation.JmsListener;

import javax.inject.Inject;
import javax.inject.Named;

@Named("distributedLockDemo")
public class OrderListenerDistributedLockDemoWithTemplate {

    private final OrderRepository orderRepository;
    private final LockTemplate lockTemplate;

    @Inject
    public OrderListenerDistributedLockDemoWithTemplate(OrderRepository orderRepository, LockTemplate lockTemplate) {
        this.orderRepository = orderRepository;
        this.lockTemplate = lockTemplate;
    }

    @JmsListener(destination = "incomingOrderQueue")
    public void processIncomingOrder(Order order) {
        if( orderRepository.exists(order.getId()) ){
            orderRepository.update(order);
        } else {
            lockTemplate.doWithLock("/order/" + order.getId(), () -> {
                if (!orderRepository.exists(order.getId())) {
                    orderRepository.persist(order);
                } else {
                    orderRepository.update(order);
                }
            });
        }
    }
}