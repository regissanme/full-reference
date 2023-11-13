package br.com.rsanme.fullreference.services;

import br.com.rsanme.fullreference.models.Task;
import br.com.rsanme.fullreference.repositories.TaskRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 18:07
 */

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Nenhuma tarefa encontrada com o id: " + id)
        );
    }

    public Task create(Task task) {
        findByName(task);

        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());
        task.setCompleted(false);

        return repository.save(task);
    }

    public Task update(Task task) {

        Task toUpdate = findById(task.getId());
        findByName(task);

        task.setCreatedAt(toUpdate.getCreatedAt());
        task.setUpdatedAt(OffsetDateTime.now());
        task.setCompleted(toUpdate.isCompleted());

        return repository.save(task);
    }

    public void delete(Long id) {
        findById(id);

        repository.deleteById(id);
    }

    public Task completeTask(Long id) {
        Task toComplete = findById(id);
        toComplete.setDeadline(OffsetDateTime.now());
        toComplete.setCompleted(true);

        return repository.save(toComplete);
    }

    private void findByName(Task task) {

        Optional<Task> byName = repository.findByName(task.getName());

        if (byName.isPresent() && task.getProject().getId().equals(byName.get().getProject().getId())) {
            throw new EntityExistsException(
                    String.format("Já existe uma tarefa com o nome %s cadastrada para o projeto %s",
                            task.getName(),
                            task.getProject().getName())
            );
        }
    }
}
