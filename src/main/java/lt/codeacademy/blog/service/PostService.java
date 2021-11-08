package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Post;
import lt.codeacademy.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post) {
        if (validateTitle(post.getTitle()) && validateDescription(post.getDescription())) {
            postRepository.save(post);
        }
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

    public void updatePost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    public Post getPost(UUID id) {
        return postRepository.getById(id);
    }
}