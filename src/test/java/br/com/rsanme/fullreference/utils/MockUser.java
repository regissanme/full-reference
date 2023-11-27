package br.com.rsanme.fullreference.utils;

import br.com.rsanme.fullreference.auth.models.UserApp;

import java.time.LocalDateTime;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 27/11/2023
 * Hora: 15:39
 */
public class MockUser {

    public static final LocalDateTime CREATED_AT = LocalDateTime.parse("2023-11-27T15:59:31.170982300");
    public static final LocalDateTime UPDATED_AT = LocalDateTime.parse("2023-11-27T17:59:31.170982300");
    public static final LocalDateTime LAST_ACCESS_AT = LocalDateTime.parse("2023-11-27T18:59:31.170982300");
    public static final long ID = 1L;
    public static final String NAME = "Teste User";
    public static final String USERNAME = "teste";
    public static final String PASSWORD = "teste";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe um usuário com o username " + USERNAME + "!";
    public static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrado nenhum usuário com o id: 1";

    public static UserApp getAdminUser() {
        UserApp user = new UserApp();
        user.setId(ID);
        user.setName(NAME);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCreatedAt(CREATED_AT);
        user.setUpdatedAt(UPDATED_AT);
        user.setLastAccessAt(LAST_ACCESS_AT);
        user.setActive(true);
        user.setRole(ROLE_ADMIN);

        return user;
    }

    public static UserApp getUser() {
        UserApp user = getAdminUser();
        user.setRole(ROLE_USER);

        return user;
    }

    public static UserApp getUserToSave(){
        return new UserApp(null, NAME, USERNAME, PASSWORD, null, null, null, null, null);
    }
}
