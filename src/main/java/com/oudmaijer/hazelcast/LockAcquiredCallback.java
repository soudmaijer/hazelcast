package com.oudmaijer.hazelcast;

public interface LockAcquiredCallback<T> {
    T execute();
}