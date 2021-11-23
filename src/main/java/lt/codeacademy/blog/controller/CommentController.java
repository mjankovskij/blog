package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.service.CommentService;
import lt.codeacademy.blog.service.BlogService;
import lt.codeacademy.blog.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
        return ResponseEntity.status(403).body("Access is denied!");
    }

    private final UserService userService;
    private final BlogService blogService;
    private final CommentService commentService;
    private final MessageSource messageSource;

    public CommentController(UserService userService, BlogService blogService, CommentService commentService, MessageSource messageSource) {
        this.userService = userService;
        this.blogService = blogService;
        this.commentService = commentService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/create")
    public String processCreate(@Valid @ModelAttribute("newComment") Comment comment,
                                BindingResult result,
                                @RequestParam String blog_id,
                                Model model,
                                Locale locale) {
        if (result.hasErrors()) {
            return "fragments/comment-form :: info-form";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        comment.setUser(user);
        comment.setBlog(blogService.getById(UUID.fromString(blog_id)));
        commentService.save(comment);
        model.addAttribute("success",
                messageSource.getMessage("lt.blog.commentSavedSuccessfully", null, locale)
        );
        return "fragments/comment-form :: info-form";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id, Locale locale) {
        try {
            Comment commentObj = commentService.getById(UUID.fromString(id));
            commentService.delete(commentObj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(messageSource.getMessage("lt.blog.commentDeletedSuccessfully", null, locale));
    }
}