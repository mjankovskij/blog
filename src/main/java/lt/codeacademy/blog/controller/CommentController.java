package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.PostRepository;
import lt.codeacademy.blog.repository.UserRepository;
import lt.codeacademy.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
            return ResponseEntity.status(403).body("Access is denied!");
    }

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;

    public CommentController(UserRepository userRepository, PostRepository postRepository, CommentService commentService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String id, @RequestParam String comment) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(auth.getName());
            commentService.save(new Comment(comment, postRepository.getById(UUID.fromString(id)), user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Post added successfully.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestParam String id, @RequestParam String comment) {
        try {
            Comment commentObj = commentService.getById(UUID.fromString(id));
            commentObj.setComment(comment);
            commentService.save(commentObj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Post updated successfully.");
    }

}