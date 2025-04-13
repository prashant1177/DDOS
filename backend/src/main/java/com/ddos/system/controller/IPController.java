package com.ddos.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ddos.system.service.IPService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class IPController {

    @Autowired
    private IPService ipService;

    @GetMapping({"/", "/**"})
    public Map<String, String> registerVisit(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        ipService.registerIP(ip);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Visit registered from IP: " + ip);
        return response;
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "Live");
        return map;
    }

    @GetMapping("/visitors")
    public Map<String, Object> getVisitors() {
        Map<String, Object> map = new HashMap<>();
        map.put("visitors", ipService.getAllIPs());
        return map;
    }

    @GetMapping("/blocked")
    public Map<String, Object> getBlocked() {
        Map<String, Object> map = new HashMap<>();
        map.put("blocked", ipService.getBlockedIPs());
        return map;
    }
      @DeleteMapping("/unblock/{ip}")
    public ResponseEntity<String> unblockIP(@PathVariable String ip) {
        ipService.unblockIP(ip);
        return ResponseEntity.ok("Unblocked IP: " + ip);
    }
}
