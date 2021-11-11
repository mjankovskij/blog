package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.CommentRepository;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        if (user.getId() == comment.getUser().getId() || auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            commentRepository.save(comment);
        }else{
            throw new IllegalArgumentException("Access is denied!");
        }
    }

    public void delete(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        if (user.getId() == comment.getUser().getId() || auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            commentRepository.delete(comment);
        }else{
            throw new IllegalArgumentException("Access is denied!");
        }
    }

    public Comment getById(UUID id) {
        return commentRepository.getById(id);
    }

//    public List<Comment> getComments() {
//        return commentRepository.findAllByOrderByDatetimeDesc();
//    }

}