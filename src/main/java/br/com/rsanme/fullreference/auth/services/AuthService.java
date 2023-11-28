package br.com.rsanme.fullreference.auth.services;

import br.com.rsanme.fullreference.auth.repository.UserAppRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 14:34
 */
@Service
public class AuthService implements UserDetailsService {

    private final UserAppRepository userAppRepository;

    public AuthService(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = userAppRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("Usuário %s não encontrado!", username));
        }
        return user;
    }
}
