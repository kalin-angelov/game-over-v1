package app.game.model;

import app.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Title cant be empty")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Description cant be empty")
    private String description;

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "ImgURL cant be empty")
    private String imgUrl;

    @ManyToOne
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank(message = "Platform cant be empty")
    private GamePlatform platform;

    @Column(nullable = false)
    private LocalDateTime createdOn;
}
