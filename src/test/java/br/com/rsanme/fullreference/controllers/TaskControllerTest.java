package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.TaskService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 18/11/2023
 * Hora: 13:15
 */
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    @InjectMocks
    private CustomApiExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
    }

    @Test
    void findAll() {

        when(service.findAll()).thenReturn(List.of(new Task()));

        given()
                .when()
                .get("task/")
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById() {
        Task task = new Task();

        when(service.findById(1L)).thenReturn(task);

        given()
                .when()
                .get("task/" + 1L)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_ThanReturnNotFound() {
        Task task = new Task();

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException("Not found!"));

        given()
                .when()
                .get("task/" + 1L)
                .then()
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void completeTask() {
    }


}