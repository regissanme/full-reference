package br.com.rsanme.fullreference.dtos;

import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 18:39
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDto {

    @NotBlank(message = "O nome da tarefa é obrigatório!")
    private String name;

    private String description;

    private String notes;

    private Long projectId;

    public Task toModel() {
        Task task = new Task();
        task.setName(this.name);
        task.setDescription(this.description);
        task.setNotes(this.notes);

        Project project = new Project();
        project.setId(this.projectId);

        task.setProject(project);

        return task;
    }
}
