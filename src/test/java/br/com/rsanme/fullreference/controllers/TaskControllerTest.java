package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.TaskCreateDto;
import br.com.rsanme.fullreference.dtos.TaskUpdateDto;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.TaskService;
import br.com.rsanme.fullreference.utils.MockTask;
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
 * Data: 18/11/2023
 * Hora: 13:15
 */
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    public static final String TASK_API_URL = "task/";

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    @InjectMocks
    private CustomApiExceptionHandler exceptionHandler;

    @Mock
    private MessageSource messageSource;

    private Task task;
    TaskCreateDto toSave;
    TaskUpdateDto toUpdate;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
        createInstances();
    }

    @Test
    void whenFindAllTasksThenReturnList() {

        when(service.findAll()).thenReturn(List.of(task));

        given()
                .when()
                .get(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdTaskThenReturnSuccess() {

        when(service.findById(1L)).thenReturn(task);

        given()
                .when()
                .get(TASK_API_URL + MockTask.ID)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdTaskThanReturnNotFound() {

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException(MockTask.ERROR_MESSAGE_NOT_FOUND));

        given()
                .when()
                .get(TASK_API_URL + 1L)
                .then()
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdTaskThanReturnBadRequest() {

        given()
                .when()
                .get(TASK_API_URL + "null")
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateTaskThenReturnCreated() {

        when(service.create(any(Task.class))).thenReturn(task);

        given()
                .body(toSave)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(CREATED.value());

        verify(service).create(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateTaskThenReturnBadRequest() {

        given()
                .body(new Task())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).create(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateTaskThenReturnAlreadyExists() {

        when(service.create(any(Task.class)))
                .thenThrow(new CustomEntityAlreadyExists(MockTask.ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(toSave)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(CONFLICT.value());

        verify(service).create(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateTaskThenReturnSuccess() {

        when(service.update(any(Task.class))).thenReturn(task);

        given()
                .body(toUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).update(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateTaskThenReturnBadRequest() {

        given()
                .body(new TaskUpdateDto())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).update(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateTaskThenReturnNotFound() {

        when(service.update(any(Task.class)))
                .thenThrow(new CustomEntityNotFoundException(MockTask.ERROR_MESSAGE_NOT_FOUND));

        given()
                .body(toUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(TASK_API_URL)
                .then()
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).update(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenDeleteTaskThenReturnSuccessMessage() {

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(TASK_API_URL + MockTask.ID)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(OK.value());

        verify(service).delete(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCompleteTaskThenReturnSuccess() {

        when(service.completeTask(anyLong())).thenReturn(task);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(TASK_API_URL + MockTask.ID)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(OK.value());

        verify(service).completeTask(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCompleteTaskThanReturnBadRequest() {

        given()
                .when()
                .put(TASK_API_URL + "null")
                .then()
                .apply(print())
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).completeTask(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCompleteTaskThenReturnNotFound() {

        when(service.completeTask(anyLong()))
                .thenThrow(new CustomEntityNotFoundException(MockTask.ERROR_MESSAGE_NOT_FOUND));

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(TASK_API_URL + MockTask.ID)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).completeTask(anyLong());
        verifyNoMoreInteractions(service);
    }

    private void createInstances() {

        task = MockTask.getTask();

        toSave = MockTask.getTaskCreateDto();

        toUpdate = MockTask.getTaskUpdateDto();
    }


}