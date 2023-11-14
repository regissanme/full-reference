package br.com.rsanme.fullreference.dtos;

import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.models.Task;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 14/11/2023
 * Hora: 18:15
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<Task> tasks = new ArrayList<>();

    public static ProjectResponse toResponse(Project project){
        ProjectResponse toResponse = new ProjectResponse();
        toResponse.setId(project.getId());
        toResponse.setName(project.getName());
        toResponse.setDescription(project.getDescription());
        toResponse.setCreatedAt(project.getCreatedAt());
        toResponse.setUpdatedAt(project.getUpdatedAt());
        toResponse.setTasks(project.getTasks());

        return toResponse;
    }
}
