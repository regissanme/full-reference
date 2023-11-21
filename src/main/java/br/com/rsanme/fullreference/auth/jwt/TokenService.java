package br.com.rsanme.fullreference.auth.jwt;

import br.com.rsanme.fullreference.auth.models.UserApp;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 15:21
 */
@Service
public class TokenService {


    @Value("#{auth.secret}")
    private String secret;

    @Value("#{auth.issuer}")
    private String issuer;

    public String generateToken(UserApp userApp) {

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userApp.getUsername())
                .withClaim("id", userApp.getId())
                .withExpiresAt(getExpiration())
                .sign(Algorithm.HMAC256(secret));
    }

    public String getSubject(String token){
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant getExpiration() {
        long expirationInMinutes = 10L;

        return LocalDateTime.now()
                .plusMinutes(expirationInMinutes)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
