package com.chriss.demo.service;

import com.chriss.demo.model.Issue;
import com.chriss.demo.model.Device;
import com.chriss.demo.repository.IssueRepository;
import com.chriss.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IssueService {
    
    @Autowired
    private IssueRepository issueRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private AuditLogService auditLogService;
    
    public Issue reportIssue(Long deviceId, String issueType, String severity, String description, String reportedBy) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            Device d = device.get();
            Issue issue = new Issue(d, issueType, severity, description, reportedBy);
            Issue saved = issueRepository.save(issue);
            
            // If issue is severe, mark device as maintenance
            if (severity.equals("CRITICAL") || severity.equals("HIGH")) {
                d.setStatus("MAINTENANCE");
                deviceRepository.save(d);
                auditLogService.logAction(d, "ISSUE_REPORTED", "SYSTEM", d.getStatus(), "MAINTENANCE", 
                    "Critical issue reported: " + issueType);
            }
            
            return saved;
        }
        return null;
    }
    
    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }
    
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }
    
    public List<Issue> getOpenIssues() {
        return issueRepository.findByStatus("OPEN");
    }
    
    public List<Issue> getIssuesByDevice(Long deviceId) {
        return issueRepository.findByDeviceIdOrderByReportedDateDesc(deviceId);
    }
    
    public List<Issue> getIssuesByStatus(String status) {
        return issueRepository.findByStatus(status);
    }
    
    public List<Issue> getIssuesByType(String issueType) {
        return issueRepository.findByIssueType(issueType);
    }
    
    public List<Issue> getIssuesBySeverity(String severity) {
        return issueRepository.findBySeverity(severity);
    }
    
    public Issue resolveIssue(Long issueId, String resolution) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isPresent()) {
            Issue i = issue.get();
            i.setStatus("RESOLVED");
            i.setResolution(resolution);
            i.setResolvedDate(LocalDateTime.now());
            Issue updated = issueRepository.save(i);
            
            // Update device status back to available
            Device d = i.getDevice();
            if (d.getStatus().equals("MAINTENANCE")) {
                d.setStatus("AVAILABLE");
                deviceRepository.save(d);
                auditLogService.logAction(d, "ISSUE_RESOLVED", "SYSTEM", "MAINTENANCE", "AVAILABLE", 
                    "Issue resolved and device available");
            }
            
            return updated;
        }
        return null;
    }
    
    public Issue closeIssue(Long issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isPresent()) {
            Issue i = issue.get();
            i.setStatus("CLOSED");
            return issueRepository.save(i);
        }
        return null;
    }
    
    public long countOpenIssues() {
        return issueRepository.countByStatus("OPEN");
    }
    
    public long countCriticalIssues() {
        return issueRepository.findBySeverity("CRITICAL").size();
    }
    
    public List<Issue> getIssuesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return issueRepository.findIssuesByDateRange(startDate, endDate);
    }
}
