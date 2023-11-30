package br.com.rsanme.fullreference.services;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import br.com.rsanme.fullreference.models.Project;
import br.com.rsanme.fullreference.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> findAll(UserApp userApp) {
        if (userApp == null) {
            return repository.findAll();
        }
        return repository.findAllByUserId(userApp.getId());

    }

    public Project findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "Não foi encontrado nenhum projeto com o id: " + id
                ));
    }

    public Project create(Project project) {
        findByName(project);

        project.setCreatedAt(OffsetDateTime.now());
        return repository.save(project);
    }

    public Project update(Project project) {
        Project toUpdate = findById(project.getId());
        findByName(project);

        project.setCreatedAt(toUpdate.getCreatedAt());
        project.setUpdatedAt(OffsetDateTime.now());

        return repository.save(project);
    }

    public void delete(Long id) {
        findById(id);

        repository.deleteById(id);
    }

    private void findByName(Project project) {

        Optional<Project> byName = repository.findByName(project.getName());

        if (byName.isPresent() && !byName.get().getId().equals(project.getId())) {
            throw new CustomEntityAlreadyExists(
                    "Já existe um projeto cadastrado com o nome: " + project.getName());
        }
    }
}
