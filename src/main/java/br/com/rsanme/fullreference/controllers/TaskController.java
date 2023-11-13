package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.TaskCreateDto;
import br.com.rsanme.fullreference.dtos.TaskUpdateDto;
import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.services.TaskService;
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
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody @Valid TaskCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(createDto.toModel()));
    }

    @PutMapping()
    public ResponseEntity<Task> update(@RequestBody @Valid TaskUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(updateDto.toModel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(String.format("Tarefa com id: %s excluída com sucesso!", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.completeTask(id));
    }
}
