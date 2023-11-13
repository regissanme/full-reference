package br.com.rsanme.fullreference.services;

import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.repositories.ProjectRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 11:43
 */
@Service
public class ProjectService {

    private ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> findAll() {
        return repository.findAll();
    }

    public Project findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Não foi encontrado nenhum projeto com o id: " + id
                ));
    }

    public Project create(Project project) {
        findByName(project);
        return repository.save(project);
    }

    public Project update(Project project) {
        findById(project.getId());
        findByName(project);

        return repository.save(project);
    }

    public void delete(Long id) {
        findById(id);

        repository.deleteById(id);
    }

    private void findByName(Project project) {

        Optional<Project> byName = repository.findByName(project.getName());

        if (byName.isPresent() && !byName.get().getId().equals(project.getId())) {
            throw new EntityExistsException(
                    "Já existe um projeto cadastrado com o nome: " + project.getName());
        }
    }
}