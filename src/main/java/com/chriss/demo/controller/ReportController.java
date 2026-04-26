package com.chriss.demo.controller;

import com.chriss.demo.model.Device;
import com.chriss.demo.model.Assignment;
import com.chriss.demo.model.Issue;
import com.chriss.demo.model.AuditLog;
import com.chriss.demo.service.DeviceService;
import com.chriss.demo.service.AssignmentService;
import com.chriss.demo.service.IssueService;
import com.chriss.demo.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @GetMapping
    public String showReports(Model model) {
        return "reports/index";
    }
    
    @GetMapping("/devices")
    public String deviceReport(Model model) {
        List<Device> allDevices = deviceService.getAllDevices();
        
        Map<String, Long> byStatus = allDevices.stream()
            .collect(Collectors.groupingBy(Device::getStatus, Collectors.counting()));
        
        Map<String, Long> byType = allDevices.stream()
            .collect(Collectors.groupingBy(Device::getDeviceType, Collectors.counting()));
        
        Map<String, Long> byCondition = allDevices.stream()
            .collect(Collectors.groupingBy(Device::getCondition, Collectors.counting()));
        
        model.addAttribute("totalDevices", allDevices.size());
        model.addAttribute("devicesByStatus", byStatus);
        model.addAttribute("devicesByType", byType);
        model.addAttribute("devicesByCondition", byCondition);
        model.addAttribute("devices", allDevices);
        
        return "reports/devices";
    }
    
    @GetMapping("/assignments")
    public String assignmentReport(Model model) {
        List<Assignment> allAssignments = assignmentService.getAllAssignments();
        List<Assignment> activeAssignments = assignmentService.getActiveAssignments();
        
        Map<String, Long> byDepartment = activeAssignments.stream()
            .collect(Collectors.groupingBy(Assignment::getDepartment, Collectors.counting()));
        
        Map<String, Long> byStatus = allAssignments.stream()
            .collect(Collectors.groupingBy(Assignment::getStatus, Collectors.counting()));
        
        model.addAttribute("totalAssignments", allAssignments.size());
        model.addAttribute("activeAssignments", activeAssignments.size());
        model.addAttribute("returnedAssignments", allAssignments.size() - activeAssignments.size());
        model.addAttribute("assignmentsByDepartment", byDepartment);
        model.addAttribute("assignmentsByStatus", byStatus);
        model.addAttribute("assignments", allAssignments);
        
        return "reports/assignments";
    }
    
    @GetMapping("/issues")
    public String issueReport(Model model) {
        List<Issue> allIssues = issueService.getAllIssues();
        List<Issue> openIssues = issueService.getOpenIssues();
        
        Map<String, Long> byStatus = allIssues.stream()
            .collect(Collectors.groupingBy(Issue::getStatus, Collectors.counting()));
        
        Map<String, Long> byType = allIssues.stream()
            .collect(Collectors.groupingBy(Issue::getIssueType, Collectors.counting()));
        
        Map<String, Long> bySeverity = allIssues.stream()
            .collect(Collectors.groupingBy(Issue::getSeverity, Collectors.counting()));
        
        model.addAttribute("totalIssues", allIssues.size());
        model.addAttribute("openIssues", openIssues.size());
        model.addAttribute("resolvedIssues", allIssues.stream().filter(i -> i.getStatus().equals("RESOLVED")).count());
        model.addAttribute("issuesByStatus", byStatus);
        model.addAttribute("issuesByType", byType);
        model.addAttribute("issuesBySeverity", bySeverity);
        model.addAttribute("issues", allIssues);
        
        return "reports/issues";
    }
    
    @GetMapping("/audit-trail")
    public String auditTrailReport(Model model) {
        List<AuditLog> auditLogs = auditLogService.getAllAuditLogs().stream()
            .sorted((a, b) -> b.getActionDate().compareTo(a.getActionDate()))
            .toList();
        
        Map<String, Long> byAction = auditLogs.stream()
            .collect(Collectors.groupingBy(AuditLog::getAction, Collectors.counting()));
        
        model.addAttribute("totalActions", auditLogs.size());
        model.addAttribute("actionsByType", byAction);
        model.addAttribute("auditLogs", auditLogs.stream().limit(100).toList());
        
        return "reports/audit-trail";
    }
    
    @PostMapping("/audit-trail/search")
    public String searchAuditTrail(@RequestParam(required = false) LocalDate fromDate,
                                  @RequestParam(required = false) LocalDate toDate,
                                  Model model) {
        List<AuditLog> results;
        
        if (fromDate != null && toDate != null) {
            LocalDateTime startDateTime = fromDate.atStartOfDay();
            LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);
            results = auditLogService.getAuditsByDateRange(startDateTime, endDateTime);
        } else {
            results = auditLogService.getAllAuditLogs();
        }
        
        results = results.stream()
            .sorted((a, b) -> b.getActionDate().compareTo(a.getActionDate()))
            .toList();
        
        model.addAttribute("auditLogs", results);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        
        return "reports/audit-trail";
    }
    
    @GetMapping("/inventory-status")
    public String inventoryStatusReport(Model model) {
        List<Device> devices = deviceService.getAllDevices();
        
        long available = deviceService.countDevicesByStatus("AVAILABLE");
        long assigned = deviceService.countDevicesByStatus("ASSIGNED");
        long maintenance = deviceService.countDevicesByStatus("MAINTENANCE");
        long damaged = deviceService.countDevicesByStatus("DAMAGED");
        
        Map<String, Long> statusDistribution = Map.of(
            "AVAILABLE", available,
            "ASSIGNED", assigned,
            "MAINTENANCE", maintenance,
            "DAMAGED", damaged
        );
        
        model.addAttribute("statusDistribution", statusDistribution);
        model.addAttribute("devices", devices);
        model.addAttribute("totalDevices", devices.size());
        
        return "reports/inventory-status";
    }
}
