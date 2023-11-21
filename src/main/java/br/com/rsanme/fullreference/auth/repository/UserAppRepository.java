package br.com.rsanme.fullreference.auth.repository;

import br.com.rsanme.fullreference.auth.models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 14:30
 */
@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    UserDetails findByUsername(String username);
}
