package com.chriss.demo.service;

import com.chriss.demo.model.Device;
import com.chriss.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeviceService {
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private AuditLogService auditLogService;
    
    public Device registerDevice(Device device) {
        Device saved = deviceRepository.save(device);
        auditLogService.logAction(saved, "REGISTERED", "SYSTEM", "", device.getStatus(), "Device registered");
        return saved;
    }
    
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }
    
    public Optional<Device> getDeviceByAssetTag(String assetTag) {
        return deviceRepository.findByAssetTag(assetTag);
    }
    
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    
    public List<Device> getDevicesByStatus(String status) {
        return deviceRepository.findByStatus(status);
    }
    
    public List<Device> getDevicesByType(String deviceType) {
        return deviceRepository.findByDeviceType(deviceType);
    }
    
    public List<Device> searchDevices(String searchTerm) {
        return deviceRepository.searchDevices(searchTerm);
    }
    
    public Device updateDevice(Long id, Device updatedDevice) {
        Optional<Device> existing = deviceRepository.findById(id);
        if (existing.isPresent()) {
            Device device = existing.get();
            String oldStatus = device.getStatus();
            
            device.setManufacturer(updatedDevice.getManufacturer());
            device.setModel(updatedDevice.getModel());
            device.setSpecifications(updatedDevice.getSpecifications());
            device.setNotes(updatedDevice.getNotes());
            device.setCondition(updatedDevice.getCondition());
            
            if (!oldStatus.equals(updatedDevice.getStatus())) {
                device.setStatus(updatedDevice.getStatus());
                auditLogService.logAction(device, "STATUS_CHANGED", "SYSTEM", oldStatus, updatedDevice.getStatus(), "Status updated");
            }
            
            return deviceRepository.save(device);
        }
        return null;
    }
    
    public void updateDeviceStatus(Long deviceId, String newStatus) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            Device d = device.get();
            String oldStatus = d.getStatus();
            d.setStatus(newStatus);
            deviceRepository.save(d);
            auditLogService.logAction(d, "STATUS_CHANGED", "SYSTEM", oldStatus, newStatus, "Status changed to " + newStatus);
        }
    }
    
    public void updateDeviceCondition(Long deviceId, String newCondition) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            Device d = device.get();
            String oldCondition = d.getCondition();
            d.setCondition(newCondition);
            deviceRepository.save(d);
            auditLogService.logAction(d, "CONDITION_CHANGED", "SYSTEM", oldCondition, newCondition, "Condition updated to " + newCondition);
        }
    }
    
    public long countDevicesByStatus(String status) {
        return deviceRepository.countByStatus(status);
    }
    
    public long getTotalDevices() {
        return deviceRepository.count();
    }
    
    public List<Device> getAvailableDevices() {
        return deviceRepository.findByStatus("AVAILABLE");
    }
    
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
