package br.com.rsanme.fullreference.utils;

import br.com.rsanme.fullreference.dtos.TaskCreateDto;
import br.com.rsanme.fullreference.dtos.TaskUpdateDto;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;

import java.time.OffsetDateTime;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 01/12/2023
 * Hora: 16:17
 */
public class MockTask {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Implements full reference to Spring Boot ecosystem";
    public static final String TASK_NAME = "Feature Spring Security with JWT";
    public static final String TASK_NOTES = "Note 01";
    public static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    public static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe uma tarefa cadastrada com o nome: Full Reference";
    public static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrada nenhuma tarefa com o id: 1";

    public static Task getTask() {

        Project project = MockProject.getProject();

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

        return task;
    }

    public static TaskCreateDto getTaskCreateDto() {
        return new TaskCreateDto(TASK_NAME, DESCRIPTION, TASK_NOTES, ID);
    }

    public static TaskUpdateDto getTaskUpdateDto() {
        return new TaskUpdateDto(ID, TASK_NAME, DESCRIPTION, TASK_NOTES);
    }
}
