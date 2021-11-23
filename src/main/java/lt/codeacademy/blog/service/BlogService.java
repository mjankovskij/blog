package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Blog;
import lt.codeacademy.blog.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository postRepository;

    public BlogService(BlogRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Blog> getBlogs(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Blog getById(UUID id) {
        return postRepository.getById(id);
    }

    public void save(Blog post) {
        postRepository.save(post);
    }

    public List<Blog> getBlogs() {
        return postRepository.findAll();
    }

    public void delete(UUID id) {
        postRepository.deleteById(postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found.")).getId());
    }

}