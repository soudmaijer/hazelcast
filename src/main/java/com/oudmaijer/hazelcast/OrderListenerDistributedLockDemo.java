package com.oudmaijer.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.springframework.jms.annotation.JmsListener;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class OrderListenerDistributedLockDemo {

    private final HazelcastInstance hazelcastInstance;
    private final OrderRepository orderRepository;

    @Inject
    public OrderListenerDistributedLockDemo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.hazelcastInstance = HazelcastClient.newHazelcastClient();
    }

    @JmsListener(destination = "incomingOrderQueue")
    public void processIncomingOrder(Order order) {
        if( orderRepository.exists(order.getId()) ){
            orderRepository.update(order);
        } else {
            ILock lock = hazelcastInstance.getLock("/order/" + order.getId());
            if (lock.tryLock()) {
                try {
                    if (!orderRepository.exists(order.getId())) {
                        orderRepository.persist(order);
                    } else {
                        orderRepository.update(order);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}