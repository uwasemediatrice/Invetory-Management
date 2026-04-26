package com.chriss.demo.config;

import com.chriss.demo.model.Device;
import com.chriss.demo.service.AuthService;
import com.chriss.demo.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AuthService authService;
    
    @Override
    public void run(String... args) throws Exception {
        authService.ensureDefaultAdmin();

        // Check if data already exists to avoid duplicates
        if (deviceService.getTotalDevices() > 0) {
            return;
        }
        
        // Initialize sample devices
        initializeDevices();
    }
    
    private void initializeDevices() {
        // Laptops
        createDevice("ASSET-001", "LAPTOP", "Dell", "Latitude 5440", "DEL-001-SN", "Core i7, 16GB RAM, 512GB SSD");
        createDevice("ASSET-002", "LAPTOP", "HP", "EliteBook 840", "HP-002-SN", "Core i5, 8GB RAM, 256GB SSD");
        createDevice("ASSET-003", "LAPTOP", "Lenovo", "ThinkPad X1", "LEN-003-SN", "Core i7, 16GB RAM, 512GB SSD");
        createDevice("ASSET-004", "LAPTOP", "Apple", "MacBook Pro", "APP-004-SN", "M1 Pro, 16GB RAM, 512GB SSD");
        
        // Desktops
        createDevice("ASSET-005", "DESKTOP", "Dell", "OptiPlex 7090", "DEL-005-SN", "Core i7, 16GB RAM, 512GB SSD");
        createDevice("ASSET-006", "DESKTOP", "HP", "EliteDesk 800", "HP-006-SN", "Core i5, 8GB RAM, 256GB SSD");
        createDevice("ASSET-007", "DESKTOP", "Lenovo", "ThinkCentre M720", "LEN-007-SN", "Core i7, 16GB RAM, 1TB HDD");
        
        // Mobile Phones
        createDevice("ASSET-008", "MOBILE_PHONE", "Apple", "iPhone 14 Pro", "APP-008-SN", "256GB Storage");
        createDevice("ASSET-009", "MOBILE_PHONE", "Samsung", "Galaxy S23", "SAM-009-SN", "256GB Storage");
        createDevice("ASSET-010", "MOBILE_PHONE", "Apple", "iPhone 13", "APP-010-SN", "128GB Storage");
    }
    
    private void createDevice(String assetTag, String deviceType, String manufacturer, 
                             String model, String serialNumber, String specifications) {
        Device device = new Device(assetTag, deviceType, manufacturer, model, serialNumber);
        device.setSpecifications(specifications);
        device.setCondition("GOOD");
        device.setStatus("AVAILABLE");
        device.setNotes("Sample data - Added during system initialization");
        deviceService.registerDevice(device);
    }
}
