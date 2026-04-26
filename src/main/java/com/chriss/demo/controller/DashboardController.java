package com.chriss.demo.controller;

import com.chriss.demo.service.DeviceService;
import com.chriss.demo.service.AssignmentService;
import com.chriss.demo.service.IssueService;
import com.chriss.demo.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Device Statistics
        model.addAttribute("totalDevices", deviceService.getTotalDevices());
        model.addAttribute("availableDevices", deviceService.countDevicesByStatus("AVAILABLE"));
        model.addAttribute("assignedDevices", deviceService.countDevicesByStatus("ASSIGNED"));
        model.addAttribute("maintenanceDevices", deviceService.countDevicesByStatus("MAINTENANCE"));
        model.addAttribute("damagedDevices", deviceService.countDevicesByStatus("DAMAGED"));
        
        // Assignment Statistics
        model.addAttribute("activeAssignments", assignmentService.countActiveAssignments());
        
        // Issue Statistics
        model.addAttribute("openIssues", issueService.countOpenIssues());
        model.addAttribute("criticalIssues", issueService.countCriticalIssues());
        
        // Recent Activities
        model.addAttribute("recentAuditLogs", auditLogService.getAllAuditLogs().stream()
            .sorted((a, b) -> b.getActionDate().compareTo(a.getActionDate()))
            .limit(10)
            .toList());
        
        return "dashboard";
    }
}
