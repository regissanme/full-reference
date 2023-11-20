package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.ProjectService;
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
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 14/11/2023
 * Hora: 11:38
 */
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Implements full reference to Spring Boot ecosystem";
    public static final String PROJECT_NAME = "Full Reference";
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    private static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    private static final String TASK_NAME = "Feature Spring Security with JWT";
    public static final String TASK_NOTES = "Note 01";
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe um projeto cadastrado com o nome: Full Reference";
    private static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrado nenhum projeto com o id: 1";

    @Mock
    private ProjectService service;

    @InjectMocks
    private ProjectController controller;

    @InjectMocks
    private CustomApiExceptionHandler exceptionHandler;

    @Mock
    private MessageSource messageSource;

    private Project project;
    private ProjectCreateDto projectCreateDto;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(controller, exceptionHandler);
        createInstances();
    }

    @Test
    void whenFindAllProjectsThenReturnList() throws Exception {

        when(service.findAll()).thenReturn(List.of(project));

        given()
                .when()
                .get("project/")
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThenSuccess() throws Exception {

        when(service.findById(1L)).thenReturn(project);

        given()
                .when()
                .get("project/" + ID)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);

    }

    @Test
    void whenFindByIdThenReturnNotFound() throws Exception {

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException(ERROR_MESSAGE_NOT_FOUND));

        given()
                .when()
                .get("project/" + 1L)
                .then()
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThanReturnBadRequest() {

        given()
                .when()
                .get("project/null")
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).findById(anyLong());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnSuccess() {

        when(service.create(any(Project.class))).thenReturn(project);

        given()
                .body(projectCreateDto)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("project")
                .then()
                .log().everything()
                .statusCode(CREATED.value());

        verify(service).create(any(Project.class));
        verifyNoMoreInteractions(service);

    }

    @Test
    void whenCreateThenReturnBadRequest() {

        given()
                .body(new Project())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("project")
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).create(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnAlreadyExists() {

        when(service.create(any(Project.class)))
                .thenThrow(new CustomEntityAlreadyExists(ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(projectCreateDto)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("project")
                .then()
                .log().everything()
                .statusCode(CONFLICT.value());

        verify(service).create(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnSuccess() throws Exception {


    }

    @Test
    void whenUpdateThenReturnNotFound() {
    }

    @Test
    void whenUpdateThenReturnAlreadyExist() {
    }

    @Test
    void whenDeleteThenReturnSuccess() {
    }

    @Test
    void whenDeleteThenReturnNotFound() {
    }

    private void createInstances() {
        project = new Project();
        project.setId(ID);
        project.setName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setCreatedAt(CREATED_AT);
        project.setUpdatedAt(UPDATED_AT);

        Task task = new Task();
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

        projectCreateDto = new ProjectCreateDto(PROJECT_NAME, DESCRIPTION);

    }


}