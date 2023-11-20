package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.TaskCreateDto;
import br.com.rsanme.fullreference.dtos.TaskUpdateDto;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 18:35
 */
@RestController
@RequestMapping("/task")
@Tag(name = "API de tarefas")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @Operation(summary = "Busca todas as tarefas.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fez a busca com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca dos dados.")
    })
    @GetMapping
    public ResponseEntity<List<Task>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Busca uma tarefa pelo Id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fez a busca pelo Id com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca dos dados.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Salva uma nova tarefa.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade salva com sucesso."),
            @ApiResponse(responseCode = "400", description = "Entidade já cadastrada."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a entidade na base de dados.")
    })
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody @Valid TaskCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(createDto.toModel()));
    }

    @Operation(summary = "Atualiza uma tarefa pelo Id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a entidade na base de dados.")
    })
    @PutMapping()
    public ResponseEntity<Task> update(@RequestBody @Valid TaskUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto.toModel()));
    }

    @Operation(summary = "Exclui uma tarefa pelo Id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir a entidade na base de dados.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(String.format("Tarefa com id: %s excluída com sucesso!", id));
    }

    @Operation(summary = "Finaliza uma tarefa pelo Id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidade finalizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada com id informado."),
            @ApiResponse(responseCode = "500", description = "Erro ao finalizar a entidade na base de dados.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.completeTask(id));
    }
}
