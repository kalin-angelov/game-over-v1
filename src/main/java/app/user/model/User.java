package app.user.model;

import app.game.model.Game;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cant be empty")
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cant be empty")
    private String password;

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Game> games = new ArrayList<>();
}
