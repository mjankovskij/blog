package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Role;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  HttpServletRequest request) {
        // if any errors, re-render the user info edit form
        if (result.hasErrors()) {
            return "fragments/register :: info-form";
        }
        // let the service layer handle the saving of the validated form fields

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String passPlain = user.getPassword();
        user.setPassword(encodedPassword);
        user.setPasswordRepeat(encodedPassword);
        Set<Role> setOfRoles = new HashSet<>();
        setOfRoles.add(new Role("ROLE_USER", user));
        user.setRoles(setOfRoles);
        userService.save(user);
        try {
            request.login(user.getUsername(), passPlain);
        } catch (ServletException ignore) {
        }
        return "fragments/register :: info-success";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(
            @ModelAttribute("user") User user,
            BindingResult result,
            HttpServletRequest request,
            Model model) {
        try {
            request.login(user.getUsername(), user.getPassword());
            return "fragments/login :: info-success";
        } catch (ServletException e) {
            model.addAttribute("error", e.getMessage());
            return "fragments/login :: info-form";
        }
    }

    @PostMapping("/logidsan")
    public ResponseEntity<?> processLoginads(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        try {
            request.login(username, password);
        } catch (IllegalArgumentException | ServletException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Login successfully.");
    }
}