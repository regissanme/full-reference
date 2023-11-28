package br.com.rsanme.fullreference.auth.services;

import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.repository.UserAppRepository;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 15:06
 */
@Service
public class UserAppService {

    private final UserAppRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAppService(UserAppRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserApp> findAll() {
        return userRepository.findAll();
    }

    public UserApp findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "Não foi encontrado nenhum usuário com o id: " + id));
    }

    public UserApp findByUsername(UserApp userApp) {
        return (UserApp) userRepository.findByUsername(userApp.getUsername());
    }

    public UserApp create(UserApp userApp) {
        entityExistsThenThrows(userApp);
        userApp.setCreatedAt(LocalDateTime.now());
        userApp.setUpdatedAt(LocalDateTime.now());
        userApp.setActive(true);
        if (userApp.getRole() == null) {
            userApp.setRole("ROLE_USER");
        }
        userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));

        return userRepository.save(userApp);
    }

    public UserApp update(UserApp toUpdate) {
        UserApp currentUser = findById(toUpdate.getId());
        if (!toUpdate.getUsername().equals(currentUser.getUsername())) {
            entityExistsThenThrows(toUpdate);
        }

        if (!toUpdate.getPassword().equals(currentUser.getPassword())) {
            toUpdate.setPassword(passwordEncoder.encode(toUpdate.getPassword()));
        }

        toUpdate.setUpdatedAt(LocalDateTime.now());
        toUpdate.setCreatedAt(currentUser.getCreatedAt());
        toUpdate.setLastAccessAt(currentUser.getLastAccessAt());
        toUpdate.setActive(currentUser.getActive());
        toUpdate.setRole(currentUser.getRole());

        return userRepository.save(toUpdate);
    }

    public void delete(Long id) {
        UserApp toDeactivate = findById(id);
        toDeactivate.setUpdatedAt(LocalDateTime.now());
        toDeactivate.setActive(false);
        userRepository.save(toDeactivate);
    }

    public void setLastAccessAt(UserApp userApp) {
        UserApp currentUser = findById(userApp.getId());
        currentUser.setLastAccessAt(currentUser.getCurrentAccessAt());
        currentUser.setCurrentAccessAt(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    private void entityExistsThenThrows(UserApp userApp) {
        UserApp byUsername = findByUsername(userApp);

        if (byUsername != null && !byUsername.getId().equals(userApp.getId())) {
            throw new CustomEntityAlreadyExists(String.format(
                    "Já existe um usuário com o username %s!", userApp.getUsername()
            ));
        }
    }
}
