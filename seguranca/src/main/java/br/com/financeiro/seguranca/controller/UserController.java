package br.com.financeiro.seguranca.controller;

import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.User;
import br.com.financeiro.seguranca.domain.enums.RoleName;
import br.com.financeiro.seguranca.exception.BadRequestException;
import br.com.financeiro.seguranca.exception.ConflitException;
import br.com.financeiro.seguranca.record.UserRecord;
import br.com.financeiro.seguranca.service.RoleService;
import br.com.financeiro.seguranca.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


/**
 * REST controller for managing {@link br.com.financeiro.seguranca.controller.UserController}.
 */
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    /**
     * {@code POST  /user} : Create a new User.
     *
     * @param userRecord the userRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new User, or with status {@code 400 (Bad Request)} if the User has already an ID.
     */
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRecord userRecord) {

        if (userRecord.id() != null) {
            throw new BadRequestException("Um novo usuário não pode ter um ID");
        }
        if (userService.checkUsername(userRecord.username())) {
            throw new ConflitException("O endereço de e-mail já está em uso. Não é possível criar uma nova conta com este e-mail.");
        }

        Role role = roleService.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: A função não foi encontrada"));

        String encodedPassword = passwordEncoder.encode(userRecord.password());
        UserRecord treatedUser = new UserRecord(null, userRecord.username(), encodedPassword, userRecord.name(), userRecord.status(), userRecord.phoneNumber(), userRecord.cpf());

        User result = userService.saveUser(treatedUser, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * {@code GET  /user} : get all the Users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Users in body.
     */
    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers(@PageableDefault(size = 15) Pageable pageable) {
        log.debug("REST request to get a pages of Usuários");

        Page<User> result = userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * {@code GET  /user/:id} : get the "id" User.
     *
     * @param id the id of the User to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the User, or with status {@code 400 (Bad Request)}.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id")UUID id) {
        log.debug("REST request to get Usuários : {}", id);
        Optional<User> result = userService.findById(id);

        if(result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            throw new BadRequestException("Usuário não encontrado!");
        }
    }

}
