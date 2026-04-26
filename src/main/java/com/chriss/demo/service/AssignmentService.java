package com.chriss.demo.service;

import com.chriss.demo.model.Assignment;
import com.chriss.demo.model.Device;
import com.chriss.demo.repository.AssignmentRepository;
import com.chriss.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private AuditLogService auditLogService;
    
    public Assignment assignDevice(Long deviceId, String employeeId, String employeeName, String department, String notes) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isPresent()) {
            Device d = device.get();
            
            // Create assignment
            Assignment assignment = new Assignment(d, employeeId, employeeName, department);
            assignment.setNotes(notes);
            Assignment saved = assignmentRepository.save(assignment);
            
            // Update device status
            d.setStatus("ASSIGNED");
            d.setDateAssigned(LocalDateTime.now());
            deviceRepository.save(d);
            
            // Log action
            auditLogService.logAction(d, "ASSIGNED", "SYSTEM", "AVAILABLE", "ASSIGNED", 
                "Assigned to employee: " + employeeName + " (" + employeeId + ")");
            
            return saved;
        }
        return null;
    }
    
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    
    public List<Assignment> getActiveAssignments() {
        return assignmentRepository.findByStatus("ACTIVE");
    }
    
    public List<Assignment> getAssignmentsByEmployee(String employeeId) {
        return assignmentRepository.findByEmployeeId(employeeId);
    }
    
    public List<Assignment> getAssignmentsByDepartment(String department) {
        return assignmentRepository.findByDepartment(department);
    }
    
    public List<Assignment> getAssignmentsByDevice(Long deviceId) {
        return assignmentRepository.findByDeviceId(deviceId);
    }
    
    public Assignment returnDevice(Long assignmentId, String condition, String notes) {
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isPresent()) {
            Assignment a = assignment.get();
            Device d = a.getDevice();
            
            // Update assignment
            a.setStatus("RETURNED");
            a.setReturnedDate(LocalDateTime.now());
            a.setNotes(notes);
            Assignment returned = assignmentRepository.save(a);
            
            // Update device
            d.setStatus("AVAILABLE");
            d.setCondition(condition);
            deviceRepository.save(d);
            
            // Log action
            auditLogService.logAction(d, "RETURNED", "SYSTEM", "ASSIGNED", "AVAILABLE", 
                "Returned by: " + a.getEmployeeName() + ". Condition: " + condition);
            
            return returned;
        }
        return null;
    }
    
    public long countActiveAssignments() {
        return assignmentRepository.countActiveAssignments();
    }
    
    public List<Assignment> getAssignmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return assignmentRepository.findAssignmentsByDateRange(startDate, endDate);
    }
}
