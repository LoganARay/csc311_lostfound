package edu.farmingdale.csc311.lostfound.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final AppUserRepo appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepo appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        String trimmedUsername = username == null ? "" : username.trim();
        String rawPassword = password == null ? "" : password;

        if (trimmedUsername.length() < 3) {
            model.addAttribute("error", "Username must be at least 3 characters.");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }

        if (rawPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }

        if (appUserRepository.existsByUsername(trimmedUsername)) {
            model.addAttribute("error", "Username is already taken.");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }

        AppUser user = new AppUser(trimmedUsername, passwordEncoder.encode(rawPassword));
        appUserRepository.save(user);
        return "redirect:/login?registered";
    }
}
