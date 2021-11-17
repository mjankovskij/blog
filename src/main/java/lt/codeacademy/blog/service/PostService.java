package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.repository.PostRepository;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(Post post) {
            postRepository.save(post);
    }

    private boolean validateTitle(String title) {
        if (title.length() >= 5 && title.length() <= 50) {
            return true;
        } else {
            throw new IllegalArgumentException("Title length must be between 5 - 50.");
        }
    }

    private boolean validateDescription(String description) {
        if (description.length() >= 50) {
            return true;
        } else {
            throw new IllegalArgumentException("Description length must be at least 50 symbols.");
        }
    }

    public List<Post> getPosts() {
        return postRepository.findAllByOrderByDatetimeDesc();
    }

    public void delete(UUID id) {
        postRepository.deleteById(postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found.")).getId());
    }

}