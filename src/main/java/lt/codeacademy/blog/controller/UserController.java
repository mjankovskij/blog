package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Role;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> processRegister(@RequestParam String username, @RequestParam String password, @RequestParam String passwordRepeat) {
        try {
            userService.validatePassword(password);
            if (!password.equals(passwordRepeat)) {
                throw new IllegalArgumentException("Passwords do not match.");
            }
            User user = new User(username, password);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            Set<Role> setOfRoles = new HashSet<>();
            setOfRoles.add(new Role("ROLE_USER", user));
            user.setRoles(setOfRoles);
            userService.save(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Registration successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> processLogin(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        try {
            request.login(username, password);
        } catch (IllegalArgumentException | ServletException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Login successfully.");
    }
}