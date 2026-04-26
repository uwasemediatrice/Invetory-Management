package com.chriss.demo.controller;

import com.chriss.demo.model.Issue;
import com.chriss.demo.model.Device;
import com.chriss.demo.service.IssueService;
import com.chriss.demo.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/issues")
public class IssueController {
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping
    public String listIssues(Model model) {
        List<Issue> issues = issueService.getAllIssues();
        model.addAttribute("issues", issues);
        model.addAttribute("openCount", issueService.countOpenIssues());
        model.addAttribute("criticalCount", issueService.countCriticalIssues());
        return "issues/list";
    }
    
    @GetMapping("/report")
    public String showReportForm(Model model) {
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "issues/report";
    }
    
    @PostMapping("/report")
    public String reportIssue(@RequestParam Long deviceId,
                             @RequestParam String issueType,
                             @RequestParam String severity,
                             @RequestParam String description,
                             @RequestParam String reportedBy,
                             RedirectAttributes redirectAttributes) {
        try {
            issueService.reportIssue(deviceId, issueType, severity, description, reportedBy);
            redirectAttributes.addFlashAttribute("success", "Issue reported successfully!");
            return "redirect:/issues";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error reporting issue: " + e.getMessage());
            return "redirect:/issues/report";
        }
    }
    
    @GetMapping("/{id}")
    public String viewIssue(@PathVariable Long id, Model model) {
        Optional<Issue> issue = issueService.getIssueById(id);
        if (issue.isPresent()) {
            model.addAttribute("issue", issue.get());
            return "issues/view";
        }
        return "redirect:/issues";
    }
    
    @GetMapping("/{id}/resolve")
    public String showResolveForm(@PathVariable Long id, Model model) {
        Optional<Issue> issue = issueService.getIssueById(id);
        if (issue.isPresent()) {
            model.addAttribute("issue", issue.get());
            return "issues/resolve";
        }
        return "redirect:/issues";
    }
    
    @PostMapping("/{id}/resolve")
    public String resolveIssue(@PathVariable Long id,
                              @RequestParam String resolution,
                              RedirectAttributes redirectAttributes) {
        try {
            issueService.resolveIssue(id, resolution);
            redirectAttributes.addFlashAttribute("success", "Issue resolved successfully!");
            return "redirect:/issues";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error resolving issue: " + e.getMessage());
            return "redirect:/issues/" + id + "/resolve";
        }
    }
    
    @PostMapping("/{id}/close")
    public String closeIssue(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            issueService.closeIssue(id);
            redirectAttributes.addFlashAttribute("success", "Issue closed successfully!");
            return "redirect:/issues";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error closing issue: " + e.getMessage());
            return "redirect:/issues/" + id;
        }
    }
    
    @GetMapping("/open")
    public String listOpenIssues(Model model) {
        List<Issue> issues = issueService.getOpenIssues();
        model.addAttribute("issues", issues);
        return "issues/open";
    }
    
    @GetMapping("/severity/{severity}")
    public String getBySeverity(@PathVariable String severity, Model model) {
        List<Issue> issues = issueService.getIssuesBySeverity(severity);
        model.addAttribute("issues", issues);
        model.addAttribute("severity", severity);
        return "issues/by-severity";
    }
    
    @GetMapping("/device/{deviceId}")
    public String getDeviceIssues(@PathVariable Long deviceId, Model model) {
        List<Issue> issues = issueService.getIssuesByDevice(deviceId);
        Optional<Device> device = deviceService.getDeviceById(deviceId);
        model.addAttribute("issues", issues);
        model.addAttribute("device", device.orElse(null));
        return "issues/device-issues";
    }
}
