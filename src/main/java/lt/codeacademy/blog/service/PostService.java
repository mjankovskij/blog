package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getById(UUID id) {
        return postRepository.getById(id);
    }

    public void save(Post post) {
            postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void delete(UUID id) {
        postRepository.deleteById(postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found.")).getId());
    }

}