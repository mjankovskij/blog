package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.UserRepository;
import lt.codeacademy.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@Controller
@EnableGlobalMethodSecurity(jsr250Enabled=true)
@RequestMapping("/post")
public class PostController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
            return ResponseEntity.status(403).body("Access is denied!");
    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String title, @RequestParam String description) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(auth.getName());
            postService.save(new Post(user, title, description));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Post added successfully.");
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id) {
        try {
            postService.delete(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Post deleted successfully.");
    }

}