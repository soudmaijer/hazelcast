package com.oudmaijer.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

class LockTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(LockTemplate.class);
    private final HazelcastInstance hazelcastInstance;

    public LockTemplate(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public <T> T doWithLock(final String lockKey, final long waitTime, final TimeUnit timeUnit, final LockAcquiredCallback<T> lockCallback) throws InterruptedException {
        final java.util.concurrent.locks.Lock lock = hazelcastInstance.getLock(lockKey);
        LOG.info("Trying to get lock [{}]", lockKey);
        if (lock.tryLock(waitTime, timeUnit)) {
            try {
                LOG.info("Got lock [{}]", lockKey);
                return lockCallback.execute();
            } finally {
                LOG.info("Released lock [{}]", lockKey);
                lock.unlock();
            }
        } else {
            LOG.warn("Could not aquire lock [{}]", lockKey);
        }
        return null;
    }

    public void doWithLock(String lockKey, LockAcquiredCallbackVoid lockCallback) {
        final java.util.concurrent.locks.Lock lock = hazelcastInstance.getLock(lockKey);
        LOG.info("Trying to get lock [{}]", lockKey);
        if (lock.tryLock()) {
            try {
                LOG.info("Got lock [{}]", lockKey);
                lockCallback.execute();
            } finally {
                LOG.info("Released lock [{}]", lockKey);
                lock.unlock();
            }
        } else {
            LOG.warn("Could not aquire lock [{}]", lockKey);
        }
    }

    public <T> T doWithLock(String lockKey, LockAcquiredCallback<T> lockCallback) {
        final java.util.concurrent.locks.Lock lock = hazelcastInstance.getLock(lockKey);
        LOG.info("Trying to get lock [{}]", lockKey);
        if (lock.tryLock()) {
            try {
                LOG.info("Got lock [{}]", lockKey);
                return lockCallback.execute();
            } finally {
                LOG.info("Released lock [{}]", lockKey);
                lock.unlock();
            }
        } else {
            LOG.warn("Could not aquire lock [{}]", lockKey);
        }
        return null;
    }

    public void shutdown() {
        hazelcastInstance.shutdown();
    }
}

