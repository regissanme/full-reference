package br.com.rsanme.fullreference.auth.controllers;

import br.com.rsanme.fullreference.auth.dtos.UserRequest;
import br.com.rsanme.fullreference.auth.dtos.UserResponse;
import br.com.rsanme.fullreference.auth.services.UserAppService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 24/11/2023
 * Hora: 15:06
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserAppService userAppService;

    public UserController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> listResponse = UserResponse.toListResponse(userAppService.findAll());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse response = UserResponse.toResponse(userAppService.findById(id), null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.toResponse(userAppService.create(request.toModel()), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserRequest request) {
        UserResponse response = UserResponse.toResponse(userAppService.update(request.toModel()), null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userAppService.delete(id);
        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }


}
