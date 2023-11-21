package br.com.rsanme.fullreference.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 14:50
 */
@Component
public record Login(
        @NotBlank(message = "O usuário é obrigatório!") String username,
        @NotBlank(message = "A senha é obrigatória!") String password
) {
}
