package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 15:15
 */
@RestController
@RequestMapping("/project")
@Tag(name = "API de projetos")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @Operation(summary = "Busca todos os projetos.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fez a busca com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca dos dados.")
    })
    @GetMapping
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(
                service.findAll(getUserApp().getId()));
    }

    @Operation(summary = "Busca um projeto pelo Id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fez a busca pelo Id com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca dos dados.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Salva um novo projeto.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entidade salva com sucesso."),
            @ApiResponse(responseCode = "400", description = "Entidade já cadastrada."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a entidade na base de dados.")
    })
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody @Valid ProjectCreateDto inputDto) {
        var principal = getUserApp();
        Project project = inputDto.toModel();
        project.setUser(principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(project));
    }

    @Operation(summary = "Atualiza um projeto pelo Id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade atualizada com sucesso."),
            @ApiResponse(responseCode = "409", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a entidade na base de dados.")
    })
    @PutMapping
    public ResponseEntity<Project> update(@RequestBody @Valid Project project) {
        return ResponseEntity.ok(service.update(project));
    }

    @Operation(summary = "Exclui um projeto pelo Id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entidade excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir a entidade na base de dados.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(String.format("Projeto com id: %s excluído com sucesso!", id));
    }

    private static UserApp getUserApp() {
        return  (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
