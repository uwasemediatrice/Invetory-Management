package com.chriss.demo.service;

import com.chriss.demo.model.AppUser;
import com.chriss.demo.model.SignupForm;
import com.chriss.demo.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private static final String SYSADMIN_USERNAME = "24RP07206";
    private static final String SYSADMIN_PASSWORD = "24RP02943";

    @Autowired
    private AppUserRepository appUserRepository;

    public Optional<AppUser> authenticate(String username, String rawPassword) {
        Optional<AppUser> existing = appUserRepository.findByUsername(username);
        if (existing.isPresent() && PasswordUtil.matches(rawPassword, existing.get().getPasswordHash())) {
            return existing;
        }
        return Optional.empty();
    }

    public AppUser registerUser(SignupForm form) {
        AppUser user = new AppUser();
        user.setFullName(form.getFullName().trim());
        user.setUsername(form.getUsername().trim());
        user.setEmail(form.getEmail().trim().toLowerCase());
        user.setPasswordHash(PasswordUtil.hash(form.getPassword()));
        user.setRole("USER");
        return appUserRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return appUserRepository.existsByEmail(email.toLowerCase());
    }

    public void ensureDefaultAdmin() {
        AppUser admin = appUserRepository.findByUsername(SYSADMIN_USERNAME).orElseGet(AppUser::new);
        admin.setFullName("System Admin");
        admin.setUsername(SYSADMIN_USERNAME);
        admin.setEmail("sysadmin@ims.local");
        admin.setPasswordHash(PasswordUtil.hash(SYSADMIN_PASSWORD));
        admin.setRole("ADMIN");
        appUserRepository.save(admin);
    }

}
