package br.com.rsanme.fullreference.services;

import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.repositories.TaskRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 21:13
 */

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    public static final long ID = 1L;

    public static final String PROJECT_NAME = "Full Reference";
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    private static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    private static final OffsetDateTime DEADLINE = OffsetDateTime.parse("2023-11-14T19:52:39.241536-03:00");
    private static final String TASK_NAME = "Feature Spring Security with JWT";
    private static final String TASK_DESCRIPTION = "Use java-jwt";
    public static final String TASK_NOTES = "Note 01";
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe uma tarefa cadastrada com o nome: Feature Spring Security with JWT para o projeto Full Reference";
    private static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrada nenhuma tarefa com o id: 1";

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository repository;

    private Task task;

    private Project project;

    @BeforeEach
    void setUp() {
        createInstances();
    }

    @Test
    void whenFindAllThenReturnList() {

        when(repository.findAll()).thenReturn(List.of(task));

        List<Task> responseList = service.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(Task.class, responseList.get(0).getClass());
        assertEquals(ID, responseList.get(0).getId());
        assertEquals(TASK_NAME, responseList.get(0).getName());
        assertEquals(TASK_DESCRIPTION, responseList.get(0).getDescription());
        assertEquals(CREATED_AT, responseList.get(0).getCreatedAt());
        assertEquals(UPDATED_AT, responseList.get(0).getUpdatedAt());
        assertEquals(DEADLINE, responseList.get(0).getDeadline());

        verify(repository, times(1))
                .findAll();

    }

    @Test
    void whenFindByIdThenSuccess() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(task));

        Task response = service.findById(ID);

        assertNotNull(response);

        assertEquals(Task.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(TASK_NAME, response.getName());
        assertEquals(TASK_DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertEquals(DEADLINE, response.getDeadline());

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
        Task toSave = new Task(null, TASK_NAME, TASK_DESCRIPTION, TASK_NOTES, true, DEADLINE, CREATED_AT, UPDATED_AT, project);

        when(repository.save(any())).thenReturn(task);

        Task response = service.create(toSave);

        assertNotNull(response);

        assertEquals(Task.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(TASK_NAME, response.getName());
        assertEquals(TASK_DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertEquals(UPDATED_AT, response.getUpdatedAt());
        assertEquals(DEADLINE, response.getDeadline());
        assertEquals(response.hashCode(), task.hashCode());
        assertTrue(response.equals(task));
        assertFalse(response.equals(toSave));
        assertFalse(response.equals(null));
        assertTrue(response.toString().contains("Task"));


        verify(repository, times(1))
                .findByName(anyString());
    }

    @Test
    void whenCreateThenThrowAlreadyExistsForProject() {

        Task toSave = new Task(null, TASK_NAME, TASK_DESCRIPTION, TASK_NOTES, true, DEADLINE, CREATED_AT, UPDATED_AT, project);

        when(repository.findByName(anyString())).thenReturn(Optional.of(task));

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

        when(repository.findById(ID)).thenReturn(Optional.of(task));
        when(repository.findByName(anyString())).thenReturn(Optional.of(task));
        when(repository.save(any())).thenReturn(task);

        Task response = service.update(task);

        assertEquals(Task.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(TASK_NAME, response.getName());
        assertEquals(TASK_DESCRIPTION, response.getDescription());
        assertEquals(CREATED_AT, response.getCreatedAt());
        assertNotEquals(UPDATED_AT, response.getUpdatedAt());
        assertEquals(DEADLINE, response.getDeadline());

        verify(repository, times(1))
                .findByName(anyString());

        verify(repository, times(1))
                .findById(anyLong());
    }

    @Test
    void whenUpdateThenThrowNotFoundException() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(task))
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

        Task toUpdate = new Task(2L, TASK_NAME, TASK_DESCRIPTION, TASK_NOTES, true, DEADLINE, CREATED_AT, UPDATED_AT, project);


        when(repository.findById(anyLong())).thenReturn(Optional.of(task));
        when(repository.findByName(anyString())).thenReturn(Optional.of(task));

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

        when(repository.findById(ID)).thenReturn(Optional.of(task));

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

    @Test
    void whenCompleteTaskThenSuccess() {

        when(repository.findById(ID)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenReturn(task);

        Task response = service.completeTask(ID);

        assertTrue(response.isCompleted());
        assertNotEquals(DEADLINE, response.getDeadline());

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, times(1))
                .save(any());
    }

    @Test
    void whenCompleteTaskThenTrowNotFound() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.completeTask(ID))
                .hasMessage(ERROR_MESSAGE_NOT_FOUND)
                .isInstanceOf(CustomEntityNotFoundException.class);

        verify(repository, times(1))
                .findById(anyLong());

        verify(repository, never())
                .save(any());
    }

    private void createInstances() {
        project = new Project();
        project.setId(ID);
        project.setName(PROJECT_NAME);


        task = new Task();
        task.setId(ID);
        task.setName(TASK_NAME);
        task.setDescription(TASK_DESCRIPTION);
        task.setNotes(TASK_NOTES);
        task.setCompleted(true);
        task.setCreatedAt(CREATED_AT);
        task.setUpdatedAt(UPDATED_AT);
        task.setDeadline(DEADLINE);
        task.setProject(project);
    }
}