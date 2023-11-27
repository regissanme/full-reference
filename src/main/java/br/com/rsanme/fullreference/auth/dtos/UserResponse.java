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
    public static UserResponse toResponse(UserApp user, String token) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getLastAccessAt(),
                user.getActive(),
                user.getRole().replace("ROLE_", ""),
                token
        );
    }

    public static List<UserResponse> toListResponse(List<UserApp> all) {
        return all.stream().map(user -> UserResponse.toResponse(user, null))
                .toList();
    }
}