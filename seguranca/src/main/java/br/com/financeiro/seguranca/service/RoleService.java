package br.com.financeiro.seguranca.service;

import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.enums.RoleName;
import br.com.financeiro.seguranca.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
