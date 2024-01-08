package br.com.financeiro.seguranca.service;

import br.com.financeiro.seguranca.broker.publisher.UserEventPublisher;
import br.com.financeiro.seguranca.broker.record.UserEventRecord;
import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.User;
import br.com.financeiro.seguranca.domain.enums.ActionType;
import br.com.financeiro.seguranca.exception.NotFoundException;
import br.com.financeiro.seguranca.exception.TokenExpiredException;
import br.com.financeiro.seguranca.record.PasswordRecord;
import br.com.financeiro.seguranca.record.UserRecord;
import br.com.financeiro.seguranca.repository.UserRepository;
import br.com.financeiro.seguranca.service.resetPassword.PasswordGenerator;
import br.com.financeiro.seguranca.service.resetPassword.TokenDecoder;
import br.com.financeiro.seguranca.service.resetPassword.TokenReset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    private final TokenDecoder tokenDecoder;

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
        user.setToken(tokenDecoder.generateUniqueTokenWithExpiration());
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

    public void completePasswordReset(PasswordRecord passwordRecord) throws TokenExpiredException {
        User user = userRepository.findByToken(passwordRecord.token())
                .orElseThrow(() -> new NotFoundException("Token não foi encontrado!"));

        TokenReset tokenReset = tokenDecoder.decodeToken(user.getToken());

        if (tokenReset.expirationTime().isAfter(LocalDateTime.now())) {
            user.setPassword(passwordEncoder.encode(passwordRecord.password()));
            user.setStatus(true);
            user.setToken(null);

            userRepository.save(user);
        } else {
            throw new TokenExpiredException("O token expirou!");
        }

    }

}
