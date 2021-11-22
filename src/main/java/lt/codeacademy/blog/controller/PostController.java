package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.service.PostService;
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
@RequestMapping("/post")
public class PostController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
        return ResponseEntity.status(403).body("Access is denied!");
    }

    private final UserService userService;
    private final PostService postService;
    private final MessageSource messageSource;

    public PostController(UserService userService, PostService postService, MessageSource messageSource) {
        this.userService = userService;
        this.postService = postService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public String processCreate(@Valid @ModelAttribute("newPost") Post post,
                                BindingResult result,
                                Model model,
                                Locale locale) {
        if (result.hasErrors()) {
            return "fragments/post-form :: info-form";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        post.setUser(user);
        postService.save(post);
        model.addAttribute("success",
                messageSource.getMessage("lt.blog.postSavedSuccessfully", null, locale)
        );
        return "fragments/post-form :: info-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id, Locale locale) {
        try {
            postService.delete(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(messageSource.getMessage("lt.blog.postDeletedSuccessfully", null, locale));
    }

    @GetMapping("/single/{id}")
    public String loadPost(Model model, @PathVariable UUID id) {
        model.addAttribute("newUser", new User());
        model.addAttribute("newPost", new Post());
        model.addAttribute("newComment", new Comment());
        model.addAttribute("post", postService.getById(id));
        return "post";
    }
}