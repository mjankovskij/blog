package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.repository.PostRepository;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository;

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void save(Post post) {
            postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAllByOrderByDatetimeDesc();
    }

    public void delete(UUID id) {
        postRepository.deleteById(postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found.")).getId());
    }

}