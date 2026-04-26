package com.chriss.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String assetTag;
    
    @Column(nullable = false)
    private String deviceType; // LAPTOP, DESKTOP, MOBILE_PHONE
    
    @Column(nullable = false)
    private String manufacturer;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private String serialNumber;
    
    @Column(nullable = false)
    private String status; // AVAILABLE, ASSIGNED, DAMAGED, MAINTENANCE
    
    private String condition; // NEW, GOOD, FAIR, POOR
    
    @Column(nullable = false)
    private LocalDateTime dateRegistered;
    
    private LocalDateTime dateAssigned;
    
    @Column(columnDefinition = "TEXT")
    private String specifications;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // Constructors
    public Device() {
        this.dateRegistered = LocalDateTime.now();
        this.status = "AVAILABLE";
        this.condition = "NEW";
    }
    
    public Device(String assetTag, String deviceType, String manufacturer, String model, String serialNumber) {
        this();
        this.assetTag = assetTag;
        this.deviceType = deviceType;
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAssetTag() {
        return assetTag;
    }
    
    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }
    
    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    
    public LocalDateTime getDateAssigned() {
        return dateAssigned;
    }
    
    public void setDateAssigned(LocalDateTime dateAssigned) {
        this.dateAssigned = dateAssigned;
    }
    
    public String getSpecifications() {
        return specifications;
    }
    
    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
