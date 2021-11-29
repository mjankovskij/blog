package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.Comment;
import lt.codeacademy.blog.repository.CommentRepository;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
            commentRepository.save(comment);
    }

    public void delete(Comment comment) {
            commentRepository.delete(comment);
    }

    public Comment getById(UUID id) {
        return commentRepository.getById(id);
    }

}