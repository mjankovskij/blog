package lt.codeacademy.blog.controller;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.UserRepository;
import lt.codeacademy.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String loadProducts(Model model, @PageableDefault(size = 5)
    @SortDefault(sort = "datetime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("posts", postService.getPosts(pageable));
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("newUser", new User());
        model.addAttribute("newPost", new Post());
        model.addAttribute("newComment", new Comment());
        return "index";
    }

}