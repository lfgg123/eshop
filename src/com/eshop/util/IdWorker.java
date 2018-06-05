package com.eshop.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdWorker {

    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * gen id
     */
    public static long nextId() {
        if (counter.get() > 999999) {
            counter.set(1);
        }
        long time = System.currentTimeMillis();
        long returnValue = time * 100 + counter.incrementAndGet();
        return returnValue;
    }

    public static long incrementAndGet() {
        return counter.incrementAndGet();
    }

    public static void main(String[] args) {

        System.out.println(IdWorker.nextId());
        System.out.println(IdWorker.nextId());
        System.out.println(IdWorker.nextId());
        System.out.println(IdWorker.nextId());
    }


}
