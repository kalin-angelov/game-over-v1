package app.web;

import app.jwt.JwtService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.UserLoginRequest;
import app.web.dto.UserLoginResponse;
import app.web.dto.UserRegisterRequest;
import app.web.dto.UserRegisterResponse;
import app.web.mapper.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static app.web.Paths.API_V1_BASE_PATH;

@RestController
@RequestMapping(API_V1_BASE_PATH + "/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser (@RequestBody UserRegisterRequest userRegisterRequest) {

        User user = userService.register(userRegisterRequest);

        UserRegisterResponse userRegisterResponse = DtoMapper.toRegisterResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegisterResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser (@RequestBody UserLoginRequest userLoginRequest) {

        userService.checkingUserCredentials(userLoginRequest);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        AuthenticationMetadata metadata = (AuthenticationMetadata) userService.loadUserByUsername(userLoginRequest.getEmail());
        final String token = jwtService.generateToken(metadata);

        UserLoginResponse userLoginResponse = DtoMapper.toLoginResponse(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userLoginResponse);
    }
}
