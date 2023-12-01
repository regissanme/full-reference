package br.com.rsanme.fullreference.utils;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 01/12/2023
 * Hora: 16:15
 */
public class MockProject {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "Implements full reference to Spring Boot ecosystem";
    public static final String PROJECT_NAME = "Full Reference";
    private static final OffsetDateTime CREATED_AT = OffsetDateTime.parse("2023-11-13T19:50:41.685439800-03:00");
    private static final OffsetDateTime UPDATED_AT = OffsetDateTime.parse("2023-11-13T19:52:39.241536-03:00");
    private static final String TASK_NAME = "Feature Spring Security with JWT";
    public static final String TASK_NOTES = "Note 01";
    public static final String ERROR_MESSAGE_ALREADY_EXISTS = "Já existe um projeto cadastrado com o nome: Full Reference";
    public static final String ERROR_MESSAGE_NOT_FOUND = "Não foi encontrado nenhum projeto com o id: 1";

    public static Project getProject() {
        UserApp user = MockUser.getUser();

        Project project = new Project();
        project.setId(ID);
        project.setName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setCreatedAt(CREATED_AT);
        project.setUpdatedAt(UPDATED_AT);
        project.setUser(user);

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

        return project;
    }

    public static ProjectCreateDto getProjectCreateDto() {
        return new ProjectCreateDto(PROJECT_NAME, DESCRIPTION, MockUser.getUser().getId());
    }
}
