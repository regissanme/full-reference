package br.com.rsanme.fullreference.auth.controllers;

import br.com.rsanme.fullreference.auth.dtos.Login;
import br.com.rsanme.fullreference.auth.dtos.UserResponse;
import br.com.rsanme.fullreference.auth.jwt.TokenService;
import br.com.rsanme.fullreference.auth.models.UserApp;
import br.com.rsanme.fullreference.auth.services.UserAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 16:18
 */
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserAppService userAppService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserAppService userAppService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userAppService = userAppService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> login(@RequestBody @Valid Login login) {
        System.out.println("Login: " + login);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        var principal = (UserApp) authenticate.getPrincipal();

        userAppService.setLastAccessAt(principal);

        String token = tokenService.generateToken(principal);

        return ResponseEntity.ok(UserResponse.toResponse(principal, token));
    }
}
