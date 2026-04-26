package com.chriss.demo.service;

import com.chriss.demo.model.AuditLog;
import com.chriss.demo.model.Device;
import com.chriss.demo.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    public AuditLog logAction(Device device, String action, String performedBy, String previousValue, String newValue, String details) {
        AuditLog log = new AuditLog(device, action, performedBy, previousValue, newValue, details);
        return auditLogRepository.save(log);
    }
    
    public List<AuditLog> getDeviceAuditHistory(Long deviceId) {
        return auditLogRepository.findDeviceAuditHistory(deviceId);
    }
    
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
    
    public List<AuditLog> getAuditsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }
    
    public List<AuditLog> getAuditsByPerformedBy(String performedBy) {
        return auditLogRepository.findByPerformedBy(performedBy);
    }
    
    public List<AuditLog> getAuditsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findAuditsByDateRange(startDate, endDate);
    }
    
    public List<AuditLog> getAuditsByDevice(Long deviceId) {
        return auditLogRepository.findByDeviceId(deviceId);
    }
}
