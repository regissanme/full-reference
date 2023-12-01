package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.exceptions.handlers.CustomApiExceptionHandler;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.services.ProjectService;
import br.com.rsanme.fullreference.utils.MockProject;
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
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 14/11/2023
 * Hora: 11:38
 */
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    public static final String PROJECT_API_URL = "project/";

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

        when(service.findAll(null)).thenReturn(List.of(project));

        given()
                .when()
                .get(PROJECT_API_URL)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(OK.value());

        verify(service).findAll(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenFindByIdThenSuccess() throws Exception {

        when(service.findById(1L)).thenReturn(project);

        given()
                .when()
                .get(PROJECT_API_URL + MockProject.ID)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).findById(anyLong());
        verifyNoMoreInteractions(service);

    }

    @Test
    void whenFindByIdThenReturnNotFound() throws Exception {

        when(service.findById(1L)).thenThrow(new CustomEntityNotFoundException(MockProject.ERROR_MESSAGE_NOT_FOUND));

        given()
                .when()
                .get(PROJECT_API_URL + 1L)
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
                .get(PROJECT_API_URL + "null")
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
                .post(PROJECT_API_URL)
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
                .post(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).create(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenCreateThenReturnAlreadyExists() {

        when(service.create(any(Project.class)))
                .thenThrow(new CustomEntityAlreadyExists(MockProject.ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(projectCreateDto)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(CONFLICT.value());

        verify(service).create(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnSuccess() throws Exception {

        when(service.update(any(Project.class))).thenReturn(project);

        given()
                .body(project)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(OK.value());

        verify(service).update(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnBadRequest() {

        given()
                .body(new Project())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(BAD_REQUEST.value());

        verify(service, never()).update(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenUpdateThenReturnNotFound() {

        when(service.update(any(Project.class)))
                .thenThrow(new CustomEntityNotFoundException(MockProject.ERROR_MESSAGE_NOT_FOUND));

        given()
                .body(project)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(NOT_FOUND.value());

        verify(service).update(any(Project.class));
        verifyNoMoreInteractions(service);

    }

    @Test
    void whenUpdateThenReturnAlreadyExist() {
        when(service.update(any(Project.class)))
                .thenThrow(new CustomEntityAlreadyExists(MockProject.ERROR_MESSAGE_ALREADY_EXISTS));

        given()
                .body(project)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(PROJECT_API_URL)
                .then()
                .log().everything()
                .statusCode(CONFLICT.value());

        verify(service).update(any(Project.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void whenDeleteThenReturnSuccess() {

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(PROJECT_API_URL + MockProject.ID)
                .then()
                .apply(print())
                .log().everything()
                .statusCode(OK.value());

        verify(service).delete(anyLong());
        verifyNoMoreInteractions(service);
    }

    private void createInstances() {
        project = MockProject.getProject();
        projectCreateDto = MockProject.getProjectCreateDto();
    }

}