package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.PostRepository;
import lt.codeacademy.blog.repository.UserRepository;
import lt.codeacademy.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
        return ResponseEntity.status(403).body("Access is denied!");
    }
//    @ExceptionHandler({TransactionSystemException.class})
//    public ResponseEntity<?> handleConstraintViolation(Exception ex) {
//        return ResponseEntity.status(403).body(ex.getMessage());
//    }

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<?> handleConstraintViolation(Exception ex, WebRequest request) {
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        assert cause != null;
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
        Optional<ConstraintViolation<?>> str = constraintViolations.stream().findFirst();
        return str.map(constraintViolation -> ResponseEntity.status(403).body("Comment " + constraintViolation.getMessage())).orElse(null);
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
        return ResponseEntity.status(200).body("Comment added successfully.");
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
        return ResponseEntity.status(200).body("Comment updated successfully.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id) {
        try {
            Comment commentObj = commentService.getById(UUID.fromString(id));
            commentService.delete(commentObj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Comment deleted successfully.");
    }
}