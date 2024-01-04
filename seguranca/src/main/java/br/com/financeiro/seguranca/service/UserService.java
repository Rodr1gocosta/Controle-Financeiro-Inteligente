package br.com.financeiro.seguranca.service;

import br.com.financeiro.seguranca.broker.publisher.UserEventPublisher;
import br.com.financeiro.seguranca.broker.record.UserEventRecord;
import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.User;
import br.com.financeiro.seguranca.domain.enums.ActionType;
import br.com.financeiro.seguranca.record.UserRecord;
import br.com.financeiro.seguranca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserEventPublisher userEventPublisher;

    private final PasswordEncoder passwordEncoder;

    /**
     * Save a User.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    public User save(User user) { return userRepository.save(user); }

    /**
     * Check a Username.
     *
     * @param username for check
     * @return true or false.
     */
    public boolean checkUsername(String username) { return userRepository.existsByUsername(username); }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) { return userRepository.findAll(pageable); }

    /**
     * Get one user by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(UserRecord userRecord, Role role) {
        String encodedPassword = passwordEncoder.encode(PasswordGenerator.generateTemporaryPassword(8));

        User user = new User();
        user.setId(userRecord.id());
        user.setName(userRecord.name());
        user.setUsername(userRecord.username());
        user.setPassword(encodedPassword);
        user.setToken(generateUniqueTokenWithExpiration());
        user.setStatus(userRecord.status());
        user.setCpf(userRecord.cpf());
        user.setPhoneNumber(userRecord.phoneNumber());
        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        if (user.getRoleList() == null) {
            user.setRoleList(new ArrayList<>());
        }

        user.getRoleList().add(role);

        User result = save(user);

        UserEventRecord userEventRecord = new UserEventRecord(result.getId(), result.getName(), result.isStatus(), result.getCpf(), result.getPhoneNumber(), ActionType.CREATE);
        userEventPublisher.publishUserEvent(userEventRecord);

        //enviar para notificação o email para o usuário criar sua senha

        return result;
    }

    private String generateUniqueTokenWithExpiration() {
        String uniqueToken = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expirationDateTime = now.plusHours(1);

        return uniqueToken + "_" + now.toString() + "_" + expirationDateTime.toString();
    }
}
