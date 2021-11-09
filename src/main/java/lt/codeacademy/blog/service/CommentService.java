package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.repository.CommentRepository;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    UserRepository userRepository;

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
            commentRepository.save(comment);
    }

}