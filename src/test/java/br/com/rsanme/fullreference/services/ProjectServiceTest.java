package br.com.rsanme.fullreference.services;

import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 19:17
 */

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    public static final long ID = 1L;
    public static final String DESCRIPTION = "Implements full reference to Spring Boot ecosystem";
    public static final String PROJECT_NAME = "Full Reference";
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    private static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    private static final String TASK_NAME = "Feature Spring Security with JWT";
    public static final String TASK_NOTES = "Note 01";
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe um projeto cadastrado com o nome: Full Reference";
    private static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrado nenhum projeto com o id: 1";
    @InjectMocks
    private ProjectService service;

    @Mock
    private ProjectRepository repository;

    private Project project;

    @BeforeEach
    void setUp() {
        createInstances();
    }

    @Test
    void whenFindAllThenReturnList() {

        when(repository.findAll()).thenReturn(List.of(project));

        List<Project> responseList = service.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(Project.class, responseList.get(0).getClass());
        assertEquals(ID, responseList.get(0).getId());
        assertEquals(PROJECT_NAME, responseList.get(0).getName());
        assertEquals(DESCRIPTION, responseList.get(0).getDescription());
        assertEquals(CREATED_AT, responseList.get(0).getCreatedAt());
        assertEquals(UPDATED_AT, responseList.get(0).getUpdatedAt());
        assertEquals(1, responseList.get(0).getTasks().size());

        verify(repository, times(1))
                .findAll();

    }

    @Test
    void whenFindByIdThenSuccess() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(project));

        Project response = service.findById(ID);

        assertNotNull(response);

        assertEquals(Project.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(PROJECT_NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertEquals(UPDATED_AT, response.getUpdatedAt());
        assertEquals(1, response.getTasks().size());

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenFindByIdThenThrowNotFoundException() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(ID))
                .hasMessage(ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenCreateThenSuccess() {
        Project toSave = new Project(null, PROJECT_NAME, DESCRIPTION, null, null, null);

        when(repository.save(any())).thenReturn(project);

        Project response = service.create(toSave);

        assertNotNull(response);

        assertEquals(Project.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(PROJECT_NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertEquals(UPDATED_AT, response.getUpdatedAt());
        assertEquals(1, response.getTasks().size());
        assertEquals(project.hashCode(), response.hashCode());
        assertTrue(response.equals(project));
        assertFalse(response.equals(null));
        assertFalse(response.equals(toSave));
        assertTrue(response.toString().contains("Project"));

        verify(repository, times(1))
                .findByName(anyString());
    }

    @Test
    void whenCreateThenThrowAlreadyExistsForProject() {

        Project toSave = new Project(null, PROJECT_NAME, DESCRIPTION, null, null, null);

        when(repository.findByName(anyString())).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> service.create(toSave))
                .hasMessage(ERROR_MESSAGE_ALREADY_EXISTS)
                .isInstanceOf(CustomEntityAlreadyExists.class);

        verify(repository, times(1))
                .findByName(anyString());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenUpdateThenSuccess() {

        when(repository.findById(ID)).thenReturn(Optional.of(project));
        when(repository.findByName(anyString())).thenReturn(Optional.of(project));
        when(repository.save(any())).thenReturn(project);

        Project response = service.update(project);

        assertEquals(Project.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(PROJECT_NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertNotEquals(UPDATED_AT, response.getUpdatedAt());
        assertEquals(1, response.getTasks().size());

        verify(repository, times(1))
                .findByName(anyString());

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenUpdateThenThrowNotFoundException() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(project))
                .hasMessage(ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .findByName(anyString());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenUpdateThenThrowAlreadyExistsForProject() {

        Project toUpdate = new Project(2L, PROJECT_NAME, null, null, null, null);

        when(repository.findById(anyLong())).thenReturn(Optional.of(project));
        when(repository.findByName(anyString())).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> service.update(toUpdate))
                .hasMessage(ERROR_MESSAGE_ALREADY_EXISTS)
                .isInstanceOf(CustomEntityAlreadyExists.class);

        verify(repository, times(1))
                .findByName(anyString());

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .save(any());
    }

    @Test
    void whenDeleteThanSuccess() {

        when(repository.findById(ID)).thenReturn(Optional.of(project));

        service.delete(ID);

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenDeleteThrowNotFoundException() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(ID))
                .hasMessage(ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .deleteById(anyLong());
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
    }
}