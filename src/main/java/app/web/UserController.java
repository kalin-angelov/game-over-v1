package app.web;

import app.security.AuthenticationMetadata;
import app.web.dto.UserInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal AuthenticationMetadata metadata) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserInfoResponse.builder()
                        .id(metadata.getId())
                        .username(metadata.getUsername())
                        .email(metadata.getEmail())
                        .imgUrl(metadata.getImgUrl())
                        .role(metadata.getRole())
                        .isActive(metadata.isActive())
                        .games(metadata.getGames())
                        .build());
    }
}