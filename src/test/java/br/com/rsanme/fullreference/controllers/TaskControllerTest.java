package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.TaskCreateDto;
import br.com.rsanme.fullreference.dtos.TaskUpdateDto;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.TaskService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
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

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Implements full reference to Spring Boot ecosystem";
    public static final String PROJECT_NAME = "Full Reference";
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    private static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    private static final String TASK_NAME = "Feature Spring Security with JWT";
    public static final String TASK_NOTES = "Note 01";
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe uma tarefa cadastrada com o nome: Full Reference";
    private static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrada nenhuma tarefa com o id: 1";

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
                .get("task/")
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
                .get("task/" + ID)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdTaskThanReturnNotFound() {

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException(ERROR_MESSAGE_NOT_FOUND));

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
    void whenFindByIdTaskThanReturnBadRequest() {

        given()
                .when()
                .get("task/null")
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
                .post("task")
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
                .post("task")
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).create(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateTaskThenReturnAlreadyExists() {

        when(service.create(any(Task.class)))
                .thenThrow(new CustomEntityAlreadyExists(ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(toSave)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("task")
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
                .put("task")
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
                .put("task")
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).update(any(Task.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateTaskThenReturnNotFound() {

        when(service.update(any(Task.class)))
                .thenThrow(new CustomEntityNotFoundException(ERROR_MESSAGE_NOT_FOUND));

        given()
                .body(toUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put("task")
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
                .delete("task/" + ID)
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
                .put("task/" + ID)
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
                .put("task/null")
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
                .thenThrow(new CustomEntityNotFoundException(ERROR_MESSAGE_NOT_FOUND));

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put("task/" + ID)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).completeTask(anyLong());
        verifyNoMoreInteractions(service);
    }

    private void createInstances() {
        Project project = new Project();
        project.setId(ID);
        project.setName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setCreatedAt(CREATED_AT);
        project.setUpdatedAt(UPDATED_AT);

        task = new Task();
        task.setId(ID);
        task.setName(TASK_NAME);
        task.setDescription(DESCRIPTION);
        task.setNotes(TASK_NOTES);
        task.setCompleted(true);
        task.setCreatedAt(CREATED_AT.plusMinutes(2));
        task.setUpdatedAt(UPDATED_AT.plusMinutes(4));
        task.setDeadline(UPDATED_AT.plusHours(8));
        task.setProject(project);

        project.setTasks(List.of(task));

        toSave = new TaskCreateDto(TASK_NAME, DESCRIPTION, TASK_NOTES);

        toUpdate = new TaskUpdateDto(ID, TASK_NAME, DESCRIPTION, TASK_NOTES);

    }


}