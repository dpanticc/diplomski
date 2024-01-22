package com.dusanpan.reservation.user;
import com.dusanpan.reservation.auth.tokens.Token;
import com.dusanpan.reservation.auth.tokens.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();

    }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        // Delete associated roles
        user.getRoles().clear();
        userRepository.save(user);

        // Fetch associated tokens
        List<Token> tokens = tokenRepository.findAllByUser(user);

        // Delete all associated tokens
        tokenRepository.deleteAll(tokens);

        // Delete user
        userRepository.deleteById(user.getId());
    }

}
