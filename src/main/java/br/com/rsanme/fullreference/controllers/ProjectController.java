package br.com.rsanme.fullreference.controllers;

import br.com.rsanme.fullreference.dtos.ProjectCreateDto;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody @Valid ProjectCreateDto inputDto) {
        return ResponseEntity.ok(service.create(inputDto.toModel()));
    }

    @PutMapping
    public ResponseEntity<Project> update(@RequestBody @Valid Project project) {
        return ResponseEntity.ok(service.update(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
