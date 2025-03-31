package app.user.service;

import app.exceptions.EmailAlreadyExistException;
import app.exceptions.InvalidInputException;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.UserLoginRequest;
import app.web.dto.UserRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(UserRegisterRequest userRegisterRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(userRegisterRequest.getEmail());

        if(optionalUser.isPresent()) {
            throw new EmailAlreadyExistException("Email address already exist.");
        }

        User user = userRepository.save(initializaUser(userRegisterRequest));
        log.info("User with Email [%s] successfully created".formatted(user.getEmail()));

        return user;
    }

    private User initializaUser(UserRegisterRequest userRegisterRequest) {
        return User.builder()
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with this email [%s] do not exist.".formatted(email)));

        return new AuthenticationMetadata(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isActive()
        );
    }

    public void checkingUserCredentials (UserLoginRequest userLoginRequest) {

        User user = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(() -> new InvalidInputException("Invalid Email or Password"));
        boolean correctPassword = passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword());

        if (!correctPassword) {
            throw new InvalidInputException("Invalid Email or Password");
        }
    }

}
