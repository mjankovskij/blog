package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.Blog;
import lt.codeacademy.blog.data.User;
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
@RequestMapping("/blog")
public class BlogController {
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException() {
        return ResponseEntity.status(403).body("Access is denied!");
    }

    private final UserService userService;
    private final BlogService blogService;
    private final MessageSource messageSource;

    public BlogController(UserService userService, BlogService blogService, MessageSource messageSource) {
        this.userService = userService;
        this.blogService = blogService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public String processCreate(@Valid @ModelAttribute("newBlog") Blog blog,
                                BindingResult result,
                                Model model,
                                Locale locale) {
        if (result.hasErrors()) {
            return "fragments/blog-form :: info-form";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        blog.setUser(user);
        blogService.save(blog);
        model.addAttribute("success",
                messageSource.getMessage("lt.blog.blogSavedSuccessfully", null, locale)
        );
        return "fragments/blog-form :: info-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id, Locale locale) {
        try {
            blogService.delete(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(messageSource.getMessage("lt.blog.blogDeletedSuccessfully", null, locale));
    }

    @GetMapping("/single/{id}")
    public String loadBlog(Model model, @PathVariable UUID id) {
        model.addAttribute("newUser", new User());
        model.addAttribute("newBlog", new Blog());
        model.addAttribute("newComment", new Comment());
        model.addAttribute("blog", blogService.getById(id));
        return "blog";
    }
}