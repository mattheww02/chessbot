package com.example.chessbot;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public Cache(int maxSize) {
        super();
        this.maxSize = maxSize;
        if (maxSize <= 0) 
            throw new RuntimeException("Cache size must be positive");
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}