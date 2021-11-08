package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.UserRepository;
import lt.codeacademy.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserRepository userRepository;
    private final PostService postService;

    public AppController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String loadProducts(Model model) {
        model.addAttribute("posts", postService.getPosts());
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "index";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestParam String title, @RequestParam String description) {
        try {
            postService.savePost(new Post(title, description));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body("Post added successfully.");
    }

}