package com.chriss.demo.controller;

import com.chriss.demo.model.AppUser;
import com.chriss.demo.model.LoginForm;
import com.chriss.demo.model.SignupForm;
import com.chriss.demo.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLogin(Model model,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String registered,
                            @RequestParam(required = false) String logout,
                            HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/dashboard";
        }

        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }

        model.addAttribute("hasError", error != null);
        model.addAttribute("registered", registered != null);
        model.addAttribute("logoutSuccess", logout != null);
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        boolean hasError = false;
        String username = loginForm.getUsername() == null ? "" : loginForm.getUsername().trim();
        String password = loginForm.getPassword() == null ? "" : loginForm.getPassword();

        if (username.isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameError", "Username is required");
            hasError = true;
        }

        if (password.isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Password is required");
            hasError = true;
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("loginForm", loginForm);
            redirectAttributes.addAttribute("error", "1");
            return "redirect:/login";
        }

        Optional<AppUser> user = authService.authenticate(username, password);
        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("loginError", "Invalid username or password");
            redirectAttributes.addFlashAttribute("loginForm", loginForm);
            redirectAttributes.addAttribute("error", "1");
            return "redirect:/login";
        }

        session.setAttribute("currentUser", user.get());
        return "redirect:/dashboard";
    }

    @GetMapping("/signup")
    public String showSignup(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/dashboard";
        }

        if (!model.containsAttribute("signupForm")) {
            model.addAttribute("signupForm", new SignupForm());
        }

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") SignupForm signupForm,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
        boolean hasError = false;
        String fullName = signupForm.getFullName() == null ? "" : signupForm.getFullName().trim();
        String normalizedUsername = signupForm.getUsername() == null ? "" : signupForm.getUsername().trim();
        String normalizedEmail = signupForm.getEmail() == null ? "" : signupForm.getEmail().trim().toLowerCase();
        String password = signupForm.getPassword() == null ? "" : signupForm.getPassword();
        String confirmPassword = signupForm.getConfirmPassword() == null ? "" : signupForm.getConfirmPassword();

        if (fullName.isEmpty()) {
            redirectAttributes.addFlashAttribute("fullNameError", "Full name is required");
            hasError = true;
        }

        if (normalizedUsername.isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameError", "Username is required");
            hasError = true;
        } else if (!normalizedUsername.matches("^[a-zA-Z0-9._-]{4,30}$")) {
            redirectAttributes.addFlashAttribute("usernameError", "Username must be 4-30 chars and can only include letters, numbers, dot, underscore, and hyphen");
            hasError = true;
        }

        if (normalizedEmail.isEmpty()) {
            redirectAttributes.addFlashAttribute("emailError", "Email is required");
            hasError = true;
        } else if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            redirectAttributes.addFlashAttribute("emailError", "Enter a valid email address");
            hasError = true;
        }

        if (password.isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Password is required");
            hasError = true;
        } else if (password.length() < 8 || password.length() > 72) {
            redirectAttributes.addFlashAttribute("passwordError", "Password must be between 8 and 72 characters");
            hasError = true;
        }

        if (confirmPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("confirmPasswordError", "Confirm password is required");
            hasError = true;
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("confirmPasswordError", "Passwords do not match");
            hasError = true;
        }

        if (!normalizedUsername.isEmpty() && authService.usernameExists(normalizedUsername)) {
            redirectAttributes.addFlashAttribute("usernameError", "Username is already taken");
            hasError = true;
        }

        if (!normalizedEmail.isEmpty() && authService.emailExists(normalizedEmail)) {
            redirectAttributes.addFlashAttribute("emailError", "Email is already registered");
            hasError = true;
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("signupForm", signupForm);
            return "redirect:/signup";
        }

        signupForm.setFullName(fullName);
        signupForm.setUsername(normalizedUsername);
        signupForm.setEmail(normalizedEmail);
        authService.registerUser(signupForm);

        return "redirect:/login?registered=1";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=1";
    }
}
