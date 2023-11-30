package br.com.rsanme.fullreference.auth.services;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.repository.UserAppRepository;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.utils.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 27/11/2023
 * Hora: 16:05
 */
@ExtendWith(MockitoExtension.class)
class UserAppServiceTest {

    @InjectMocks
    private UserAppService service;

    @Mock
    private UserAppRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserApp user;

    @BeforeEach
    void setUp() {

        createIntances();
    }

    @Test
    void whenFindAllThenReturnList() {

        when(repository.findAll()).thenReturn(List.of(user));

        List<UserApp> responseList = service.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(UserApp.class, responseList.get(0).getClass());
        assertEquals(MockUser.ID, responseList.get(0).getId());
        assertEquals(MockUser.NAME, responseList.get(0).getName());
        assertEquals(MockUser.USERNAME, responseList.get(0).getUsername());
        assertEquals(MockUser.PASSWORD, responseList.get(0).getPassword());
        assertEquals(MockUser.CREATED_AT, responseList.get(0).getCreatedAt());
        assertEquals(MockUser.UPDATED_AT, responseList.get(0).getUpdatedAt());
        assertEquals(MockUser.LAST_ACCESS_AT, responseList.get(0).getLastAccessAt());

        verify(repository, times(1))
                .findAll();

    }

    @Test
    void whenFindByIdThenSuccess() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        UserApp response = service.findById(MockUser.ID);

        assertNotNull(response);

        assertEquals(UserApp.class, response.getClass());
        assertEquals(MockUser.ID, response.getId());
        assertEquals(MockUser.NAME, response.getName());
        assertEquals(MockUser.USERNAME, response.getUsername());
        assertEquals(MockUser.PASSWORD, response.getPassword());
        assertEquals(MockUser.CREATED_AT, response.getCreatedAt());
        assertEquals(MockUser.UPDATED_AT, response.getUpdatedAt());
        assertEquals(MockUser.LAST_ACCESS_AT, response.getLastAccessAt());

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenFindByIdThenThrowNotFoundException() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(MockUser.ID))
                .hasMessage(MockUser.ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenCreateThenSuccess() {
        UserApp toSave = MockUser.getUserToSave();

        when(repository.save(any())).thenReturn(user);

        UserApp response = service.create(toSave);

        assertNotNull(response);

        assertEquals(UserApp.class, response.getClass());
        assertEquals(MockUser.ID, response.getId());
        assertEquals(MockUser.NAME, response.getName());
        assertEquals(MockUser.USERNAME, response.getUsername());
        assertEquals(MockUser.PASSWORD, response.getPassword());
        assertEquals(MockUser.CREATED_AT, response.getCreatedAt());
        assertEquals(MockUser.UPDATED_AT, response.getUpdatedAt());
        assertEquals(MockUser.LAST_ACCESS_AT, response.getLastAccessAt());
        assertEquals(1, response.getAuthorities().size());
        assertEquals(MockUser.ROLE_USER ,response.getRole());
        assertTrue(response.isAccountNonExpired());
        assertTrue(response.isAccountNonLocked());
        assertTrue(response.isCredentialsNonExpired());
        assertTrue(response.isEnabled());
        assertEquals(user.hashCode(), response.hashCode());
        assertTrue(response.equals(user));
        assertFalse(response.equals(null));
        assertFalse(response.equals(toSave));
        assertTrue(response.toString().contains("UserApp"));

        verify(repository, times(1))
                .findByUsername(anyString());
    }

    @Test
    void whenCreateThenThrowAlreadyExistsForUserApp() {

        UserApp toSave = MockUser.getUserToSave();

        when(repository.findByUsername(anyString())).thenReturn(user);

        assertThatThrownBy(() -> service.create(toSave))
                .hasMessage(MockUser.ERROR_MESSAGE_ALREADY_EXISTS)
                .isInstanceOf(CustomEntityAlreadyExists.class);

        verify(repository, times(1))
                .findByUsername(anyString());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenUpdateThenSuccess() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(user);

        UserApp response = service.update(user);

        assertEquals(UserApp.class, response.getClass());
        assertEquals(MockUser.ID, response.getId());
        assertEquals(MockUser.NAME, response.getName());
        assertEquals(MockUser.USERNAME, response.getUsername());
        assertEquals(MockUser.PASSWORD, response.getPassword());
        assertEquals(MockUser.CREATED_AT, response.getCreatedAt());
        assertEquals(MockUser.LAST_ACCESS_AT, response.getLastAccessAt());

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository).save(any(UserApp.class));
    }

    @Test
    void whenUpdateChangingPasswordThenSuccess() {

        UserApp toUpdate = MockUser.getUser();
        toUpdate.setPassword("new password");

        when(repository.findById(MockUser.ID)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(user);

        UserApp response = service.update(toUpdate);

        assertEquals(UserApp.class, response.getClass());
        assertEquals(MockUser.ID, response.getId());
        assertEquals(MockUser.NAME, response.getName());
        assertEquals(MockUser.USERNAME, response.getUsername());
        assertEquals(MockUser.PASSWORD, response.getPassword());
        assertEquals(MockUser.CREATED_AT, response.getCreatedAt());
        assertEquals(MockUser.LAST_ACCESS_AT, response.getLastAccessAt());

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository).save(any(UserApp.class));
    }

    @Test
    void whenUpdateThenThrowNotFoundException() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(user))
                .hasMessage(MockUser.ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .findByUsername(anyString());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenUpdateThenThrowAlreadyExistsForUserApp() {

        UserApp toUpdate = MockUser.getUser();
        user.setId(2L);
        user.setUsername("teste2");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.findByUsername(anyString())).thenReturn(user);

        assertThatThrownBy(() -> service.update(toUpdate))
                .hasMessage(MockUser.ERROR_MESSAGE_ALREADY_EXISTS)
                .isInstanceOf(CustomEntityAlreadyExists.class);

        verify(repository, times(1))
                .findByUsername(anyString());

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenDeleteThanSuccess() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.of(user));

        service.delete(MockUser.ID);

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenDeleteThrowNotFoundException() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(MockUser.ID))
                .hasMessage(MockUser.ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .deleteById(anyLong());
    }

    @Test
    void whenSetLastAccessAtThanSuccess() {

        when(repository.findById(MockUser.ID)).thenReturn(Optional.of(user));

        service.setLastAccessAt(user);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository).save(any(UserApp.class));
    }

    @Test
    void whenSetLastAccessAtThrowNotFoundException(){
        when(repository.findById(MockUser.ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.setLastAccessAt(user))
                .hasMessage(MockUser.ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository)
                .findById(anyLong());

        verify(repository, never())
                .save(any(UserApp.class));
    }

    private void createIntances() {
        user = MockUser.getUser();
    }
}