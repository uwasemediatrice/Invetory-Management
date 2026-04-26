package com.chriss.demo.controller;

import com.chriss.demo.model.Assignment;
import com.chriss.demo.model.Device;
import com.chriss.demo.service.AssignmentService;
import com.chriss.demo.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping
    public String listAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        model.addAttribute("assignments", assignments);
        model.addAttribute("activeCount", assignmentService.countActiveAssignments());
        return "assignments/list";
    }
    
    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        List<Device> availableDevices = deviceService.getAvailableDevices();
        model.addAttribute("availableDevices", availableDevices);
        return "assignments/assign";
    }
    
    @PostMapping("/assign")
    public String assignDevice(@RequestParam Long deviceId,
                              @RequestParam String employeeId,
                              @RequestParam String employeeName,
                              @RequestParam String department,
                              @RequestParam(required = false) String notes,
                              RedirectAttributes redirectAttributes) {
        try {
            assignmentService.assignDevice(deviceId, employeeId, employeeName, department, notes);
            redirectAttributes.addFlashAttribute("success", "Device assigned successfully to " + employeeName);
            return "redirect:/assignments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error assigning device: " + e.getMessage());
            return "redirect:/assignments/assign";
        }
    }
    
    @GetMapping("/{id}")
    public String viewAssignment(@PathVariable Long id, Model model) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        if (assignment.isPresent()) {
            model.addAttribute("assignment", assignment.get());
            return "assignments/view";
        }
        return "redirect:/assignments";
    }
    
    @GetMapping("/{id}/return")
    public String showReturnForm(@PathVariable Long id, Model model) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        if (assignment.isPresent()) {
            model.addAttribute("assignment", assignment.get());
            return "assignments/return";
        }
        return "redirect:/assignments";
    }
    
    @PostMapping("/{id}/return")
    public String returnDevice(@PathVariable Long id,
                              @RequestParam String condition,
                              @RequestParam(required = false) String notes,
                              RedirectAttributes redirectAttributes) {
        try {
            assignmentService.returnDevice(id, condition, notes);
            redirectAttributes.addFlashAttribute("success", "Device returned successfully!");
            return "redirect:/assignments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error returning device: " + e.getMessage());
            return "redirect:/assignments/" + id + "/return";
        }
    }
    
    @GetMapping("/active")
    public String listActiveAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getActiveAssignments();
        model.addAttribute("assignments", assignments);
        return "assignments/active";
    }
    
    @GetMapping("/department/{department}")
    public String getByDepartment(@PathVariable String department, Model model) {
        List<Assignment> assignments = assignmentService.getAssignmentsByDepartment(department);
        model.addAttribute("assignments", assignments);
        model.addAttribute("department", department);
        return "assignments/by-department";
    }
    
    @GetMapping("/employee/{employeeId}")
    public String getByEmployee(@PathVariable String employeeId, Model model) {
        List<Assignment> assignments = assignmentService.getAssignmentsByEmployee(employeeId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("employeeId", employeeId);
        return "assignments/by-employee";
    }
}
