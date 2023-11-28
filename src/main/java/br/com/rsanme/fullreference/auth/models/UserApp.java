package br.com.rsanme.fullreference.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 21/11/2023
 * Hora: 13:58
 */
@Entity
@Table(name = "users_app")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserApp implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "O usuário é obrigatório!")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "A senha é obrigatória!")
    @Column(nullable = false)
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastAccessAt;

    private LocalDateTime currentAccessAt;

    @Column(columnDefinition = "boolean default true")
    private Boolean active;

    @NotBlank(message = "A senha é obrigatória!")
    @Column(nullable = false)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        "ROLE_USER"
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserApp userApp = (UserApp) object;
        return Objects.equals(id, userApp.id) && Objects.equals(username, userApp.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
