package org.cris6h16.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public interface UserComponent {
    Long create(CreateUserInput user);
    Optional<UserOutput> findByEmailAndEnabled(String email, boolean enabled);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    void updatePasswordByEmail(String email, String password);

    boolean existsByEmail(String email);

    Optional<UserOutput> findByIdAndEnable(Long id, boolean enabled);

    boolean existsByIdAndEnabled(Long id, boolean enabled);

    void deleteAll();


    Page<UserOutput> findAll(Pageable pageable);

    void updateAuthoritiesById(Long id, Set<String> authorities);

    void adjustBalanceById(Long id, BigDecimal delta);

    /**
     * Usado para validar y limpiar la contrasena, unico metodo de validacion incluido, ya que al componente la contrasena siempre llega encriptada, lo cual las validaciones de contrasena pierden sentido
     * @param password
     * @return la contrasena limpia ( trim, etc ) solo si es valida, de otro modo lanzara una exception
     */
    String isPassValidElseThrow(String password);

    void deleteByEmail(String email);

    boolean existsByEmailAndEnabled(String email, boolean enabled);
}
