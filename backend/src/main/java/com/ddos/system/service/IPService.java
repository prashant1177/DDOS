package com.ddos.system.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class IPService {

   private final Map<String, Integer> visitors = new HashMap<>();
private final Set<String> blockedIPs = new HashSet<>();
private final Map<String, Deque<Long>> ipRequestTimes = new HashMap<>();

private static final int REQUEST_LIMIT = 10; // per TIME_WINDOW_MS
private static final int TIME_WINDOW_MS = 3000; // 3 seconds
private static final int REQUEST_THRESHOLD = 100; // total limit

public void registerIP(String ip) {
    if (isBlocked(ip)) return;

    // Rate limit check (requests in the time window)
    long now = System.currentTimeMillis();
    ipRequestTimes.putIfAbsent(ip, new ArrayDeque<>());
    Deque<Long> timestamps = ipRequestTimes.get(ip);

    timestamps.addLast(now);
    while (!timestamps.isEmpty() && timestamps.peekFirst() < now - TIME_WINDOW_MS) {
        timestamps.pollFirst();
    }

    // Total request count tracking
    visitors.put(ip, visitors.getOrDefault(ip, 0) + 1);

    // Blocking conditions
    if (timestamps.size() > REQUEST_LIMIT || visitors.get(ip) > REQUEST_THRESHOLD) {
        blockedIPs.add(ip);
    }
}


    public List<Map<String, Object>> getAllIPs() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : visitors.entrySet()) {
            Map<String, Object> ipData = new HashMap<>();
            ipData.put("ip", entry.getKey());
            ipData.put("requestCount", entry.getValue());
            result.add(ipData);
        }
        return result;
    }

    public List<Map<String, Object>> getBlockedIPs() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (String ip : blockedIPs) {
            Map<String, Object> ipData = new HashMap<>();
            ipData.put("ip", ip);
            ipData.put("requestCount", visitors.getOrDefault(ip, 0));
            ipData.put("blocked", true);
            result.add(ipData);
        }
        return result;
    }

    public boolean isBlocked(String ip) {
        return blockedIPs.contains(ip);
    }

    // 🆕 Add this method here
    public void unblockIP(String ip) {
        blockedIPs.remove(ip);
        ipRequestTimes.remove(ip);
        visitors.remove(ip);
    }
    
}
