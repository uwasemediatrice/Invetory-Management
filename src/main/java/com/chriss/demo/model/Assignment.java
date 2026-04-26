package com.chriss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    
    @Column(nullable = false)
    private String employeeId;
    
    @Column(nullable = false)
    private String employeeName;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private LocalDateTime assignedDate;
    
    private LocalDateTime returnedDate;
    
    @Column(nullable = false)
    private String status; // ACTIVE, RETURNED
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // Constructors
    public Assignment() {
        this.assignedDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    public Assignment(Device device, String employeeId, String employeeName, String department) {
        this();
        this.device = device;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
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
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }
    
    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }
    
    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
