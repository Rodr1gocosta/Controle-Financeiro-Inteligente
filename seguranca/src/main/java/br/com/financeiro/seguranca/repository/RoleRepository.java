package br.com.financeiro.seguranca.repository;

import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleName name);
}
