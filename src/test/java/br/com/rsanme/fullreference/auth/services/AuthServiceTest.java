package br.com.rsanme.fullreference.auth.services;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.repository.UserAppRepository;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.utils.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 28/11/2023
 * Hora: 00:10
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserAppRepository repository;

    private UserApp user;

    @BeforeEach
    void setUp() {
        user = MockUser.getUser();
    }

    @Test
    void wheLoadUserByUsernameThenSuccess() {

        when(repository.findByUsername(anyString())).thenReturn(user);

        service.loadUserByUsername(user.getUsername());

        verify(repository).findByUsername(anyString());
    }

    @Test
    void whenLoadUserByUsernameThrowNotFoundException() {

        when(repository.findByUsername(anyString())).thenReturn(null);

        assertThatThrownBy(() -> service.loadUserByUsername(user.getUsername()))
                .hasMessage(MockUser.ERROR_MESSAGE_USER_NOT_FOUND)
                .isInstanceOf(UsernameNotFoundException.class);

        verify(repository)
                .findByUsername(anyString());
    }
}