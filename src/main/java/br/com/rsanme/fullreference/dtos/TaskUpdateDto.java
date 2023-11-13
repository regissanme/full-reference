package br.com.rsanme.fullreference.dtos;

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
 * Hora: 18:42
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDto {

    private Long id;

    @NotBlank(message = "O nome da tarefa é obrigatório!")
    private String name;

    private String description;

    private String notes;

    public Task toModel() {
        Task task = new Task();
        task.setId(this.id);
        task.setName(this.name);
        task.setDescription(this.description);
        task.setNotes(this.notes);

        return task;
    }
}
