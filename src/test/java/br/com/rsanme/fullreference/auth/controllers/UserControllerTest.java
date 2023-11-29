package br.com.rsanme.fullreference.auth.controllers;

import br.com.rsanme.fullreference.auth.dtos.UserRequest;
import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.services.UserAppService;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.utils.MockUser;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 28/11/2023
 * Hora: 23:01
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private String apiUrl;

    @Mock
    private UserAppService service;

    @InjectMocks
    private UserController controller;

    @InjectMocks
    private CustomApiExceptionHandler exceptionHandler;

    @Mock
    private MessageSource messageSource;

    private UserApp userApp;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
        createInstances();
    }

    @Test
    void whenFindAllUsersThenReturnList() throws Exception {

        when(service.findAll()).thenReturn(List.of(userApp));

        given()
                .when()
                .get(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value());

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThenSuccess() throws Exception {

        when(service.findById(1L)).thenReturn(userApp);

        given()
                .when()
                .get(apiUrl + MockUser.ID)
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThenReturnNotFound() throws Exception {

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException(MockUser.ERROR_MESSAGE_NOT_FOUND));

        given()
                .when()
                .get(apiUrl + 1L)
                .then()
                .log().ifValidationFails()
                .statusCode(NOT_FOUND.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThanReturnBadRequest() {

        given()
                .when()
                .get(apiUrl + "null")
                .then()
                .log().ifValidationFails()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnSuccess() {

        when(service.create(any(UserApp.class))).thenReturn(userApp);

        given()
                .body(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(CREATED.value());

        verify(service).create(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnBadRequest() {

        given()
                .body(new UserRequest(null, null, null, null))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).create(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnAlreadyExists() {

        when(service.create(any(UserApp.class)))
                .thenThrow(new CustomEntityAlreadyExists(MockUser.ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(CONFLICT.value());

        verify(service).create(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnSuccess() throws Exception {

        when(service.update(any(UserApp.class))).thenReturn(userApp);

        given()
                .body(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value());

        verify(service).update(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnBadRequest() {

        given()
                .body(new UserRequest(null, null, null, null))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).update(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnNotFound() {

        when(service.update(any(UserApp.class)))
                .thenThrow(new CustomEntityNotFoundException(MockUser.ERROR_MESSAGE_NOT_FOUND));

        given()
                .body(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(NOT_FOUND.value());

        verify(service).update(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnAlreadyExist() {
        when(service.update(any(UserApp.class)))
                .thenThrow(new CustomEntityAlreadyExists(MockUser.ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(apiUrl)
                .then()
                .log().ifValidationFails()
                .statusCode(CONFLICT.value());

        verify(service).update(any(UserApp.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenDeleteThenReturnSuccess() {

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(apiUrl + MockUser.ID)
                .then()
                .apply(print())
                .log().ifValidationFails()
                .statusCode(OK.value());

        verify(service).delete(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenDeleteThrowsNotFound() {

        doThrow(new CustomEntityNotFoundException(MockUser.ERROR_MESSAGE_NOT_FOUND)).when(service).delete(anyLong());

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(apiUrl + MockUser.ID)
                .then()
                .apply(print())
                .log().ifValidationFails()
                .statusCode(NOT_FOUND.value());

        verify(service).delete(anyLong());
        verifyNoMoreInteractions(service);
    }

    private void createInstances() {

        apiUrl = "users/";

        userApp = MockUser.getUser();
        userRequest = MockUser.getUserRequest();
    }
}