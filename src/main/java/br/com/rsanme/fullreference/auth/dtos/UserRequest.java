package br.com.rsanme.fullreference.auth.dtos;

import br.com.rsanme.fullreference.auth.models.UserApp;
import jakarta.validation.constraints.NotBlank;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 24/11/2023
 * Hora: 15:14
 */
public record UserRequest(

        Long id,
        @NotBlank(message = "O nome é obrigatório!") String name,
        @NotBlank(message = "O nome é obrigatório!") String username,

        @NotBlank(message = "A senha é obrigatória!") String password

) {

    public UserApp toModel() {
        UserApp user = new UserApp();
        user.setId(this.id);
        user.setName(this.name);
        user.setUsername(this.username);
        user.setPassword(this.password);

        return user;
    }

}
