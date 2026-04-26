package com.chriss.demo.controller;

import com.chriss.demo.model.Device;
import com.chriss.demo.service.DeviceService;
import com.chriss.demo.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/devices")
public class DeviceController {
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @GetMapping
    public String listDevices(Model model) {
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        model.addAttribute("totalDevices", deviceService.getTotalDevices());
        model.addAttribute("availableCount", deviceService.countDevicesByStatus("AVAILABLE"));
        model.addAttribute("assignedCount", deviceService.countDevicesByStatus("ASSIGNED"));
        model.addAttribute("maintenanceCount", deviceService.countDevicesByStatus("MAINTENANCE"));
        return "devices/list";
    }
    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("device", new Device());
        return "devices/register";
    }
    
    @PostMapping("/register")
    public String registerDevice(@ModelAttribute Device device, RedirectAttributes redirectAttributes) {
        try {
            Device registered = deviceService.registerDevice(device);
            redirectAttributes.addFlashAttribute("success", "Device registered successfully! Asset Tag: " + registered.getAssetTag());
            return "redirect:/devices";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error registering device: " + e.getMessage());
            return "redirect:/devices/register";
        }
    }
    
    @GetMapping("/{id}")
    public String viewDevice(@PathVariable Long id, Model model) {
        Optional<Device> device = deviceService.getDeviceById(id);
        if (device.isPresent()) {
            model.addAttribute("device", device.get());
            model.addAttribute("auditHistory", auditLogService.getDeviceAuditHistory(id));
            return "devices/view";
        }
        return "redirect:/devices";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Device> device = deviceService.getDeviceById(id);
        if (device.isPresent()) {
            model.addAttribute("device", device.get());
            return "devices/edit";
        }
        return "redirect:/devices";
    }
    
    @PostMapping("/{id}/edit")
    public String updateDevice(@PathVariable Long id, @ModelAttribute Device device, RedirectAttributes redirectAttributes) {
        try {
            deviceService.updateDevice(id, device);
            redirectAttributes.addFlashAttribute("success", "Device updated successfully!");
            return "redirect:/devices/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating device: " + e.getMessage());
            return "redirect:/devices/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteDevice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            deviceService.deleteDevice(id);
            redirectAttributes.addFlashAttribute("success", "Device deleted successfully!");
            return "redirect:/devices";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting device: " + e.getMessage());
            return "redirect:/devices/" + id;
        }
    }
    
    @GetMapping("/search")
    public String searchDevices(@RequestParam String searchTerm, Model model) {
        List<Device> devices = deviceService.searchDevices(searchTerm);
        model.addAttribute("devices", devices);
        model.addAttribute("searchTerm", searchTerm);
        return "devices/search";
    }
    
    @GetMapping("/by-type/{type}")
    public String getDevicesByType(@PathVariable String type, Model model) {
        List<Device> devices = deviceService.getDevicesByType(type);
        model.addAttribute("devices", devices);
        model.addAttribute("deviceType", type);
        return "devices/by-type";
    }
    
    @GetMapping("/by-status/{status}")
    public String getDevicesByStatus(@PathVariable String status, Model model) {
        List<Device> devices = deviceService.getDevicesByStatus(status);
        model.addAttribute("devices", devices);
        model.addAttribute("status", status);
        return "devices/by-status";
    }
}
