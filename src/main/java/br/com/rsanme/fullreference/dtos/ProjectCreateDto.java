package br.com.rsanme.fullreference.dtos;

import br.com.rsanme.fullreference.models.Project;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 16:04
 */
@Component
@Data
@AllArgsConstructor @NoArgsConstructor
public class ProjectCreateDto {

    @NotBlank
    private String name;

    private String description;

    public Project toModel() {
        Project project = new Project();
        project.setName(this.name);
        project.setDescription(this.description);

        return project;
    }
}
