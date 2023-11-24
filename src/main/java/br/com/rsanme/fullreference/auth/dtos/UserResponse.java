package br.com.rsanme.fullreference.auth.dtos;

import br.com.rsanme.fullreference.auth.models.UserApp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 14:57
 */
public record UserResponse(
        Long id,
        String name,
        String username,
        LocalDateTime createdAt,
        LocalDateTime lastAccessAt,
        Boolean active,
        String role,
        String token
) {
    public static UserResponse toResponse(UserApp app, String token) {
        return new UserResponse(
                app.getId(),
                app.getName(),
                app.getUsername(),
                app.getCreatedAt(),
                app.getLastAccessAt(),
                app.getActive(),
                app.getRole(),
                token
        );
    }

    public static List<UserResponse> toListResponse(List<UserApp> all) {
        return all.stream().map(user -> UserResponse.toResponse(user, null))
                .toList();
    }
}