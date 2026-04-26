package com.chriss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    
    @Column(nullable = false)
    private String action; // REGISTERED, ASSIGNED, RETURNED, DAMAGED, REPAIRED, STATUS_CHANGED, etc.
    
    @Column(nullable = false)
    private LocalDateTime actionDate;
    
    @Column(nullable = false)
    private String performedBy;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    @Column(nullable = false)
    private String previousValue;
    
    @Column(nullable = false)
    private String newValue;
    
    // Constructors
    public AuditLog() {
        this.actionDate = LocalDateTime.now();
    }
    
    public AuditLog(Device device, String action, String performedBy, String previousValue, String newValue, String details) {
        this();
        this.device = device;
        this.action = action;
        this.performedBy = performedBy;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.details = details;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Device getDevice() {
        return device;
    }
    
    public void setDevice(Device device) {
        this.device = device;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public LocalDateTime getActionDate() {
        return actionDate;
    }
    
    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public String getPreviousValue() {
        return previousValue;
    }
    
    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
