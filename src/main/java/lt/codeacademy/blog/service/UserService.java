package lt.codeacademy.blog.service;

import lt.codeacademy.blog.data.User;
import lt.codeacademy.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository postRepository) {
        this.userRepository = postRepository;
    }

    public void save(User user) {
        if (!(userRepository.findByUsername(user.getUsername()) == null)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (validateUsername(user.getUsername())) {
            userRepository.save(user);
        }
    }

    private boolean validateUsername(String username) {
        if (username.length() >= 3 && username.length() <= 20) {
            return true;
        } else {
            throw new IllegalArgumentException("Username length must be between 3 - 20.");
        }
    }

    public boolean validatePassword(String password) {
        if (password.length() >= 8) {
            return true;
        } else {
            throw new IllegalArgumentException("Password length must be at least 8 symbols.");
        }
    }
}