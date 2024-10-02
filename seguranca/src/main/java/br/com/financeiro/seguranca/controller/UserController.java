package br.com.financeiro.seguranca.controller;

import br.com.financeiro.seguranca.domain.Role;
import br.com.financeiro.seguranca.domain.User;
import br.com.financeiro.seguranca.domain.enums.RoleName;
import br.com.financeiro.seguranca.exception.BadRequestException;
import br.com.financeiro.seguranca.exception.ConflitException;
import br.com.financeiro.seguranca.exception.NotFoundException;
import br.com.financeiro.seguranca.record.PasswordRecord;
import br.com.financeiro.seguranca.record.ResetPasswordRecord;
import br.com.financeiro.seguranca.record.UserRecord;
import br.com.financeiro.seguranca.service.RoleService;
import br.com.financeiro.seguranca.service.UserService;
import br.com.financeiro.seguranca.service.resetPassword.PasswordGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


/**
 * REST controller for managing {@link User}.
 */
@Log4j2
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordGenerator passwordGenerator;


    /**
     * {@code POST  /user} : Create a new User.
     *
     * @param userRecord the userRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new User, or with status {@code 400 (Bad Request)} if the User has already an ID.
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRecord userRecord) {
        log.info("REST request to create User : {}", userRecord);

        if (userRecord.id() != null) {
            throw new BadRequestException("Um novo usuário não pode ter um ID");
        }
        if (userService.checkUsername(userRecord.username())) {
            throw new ConflitException("O endereço de e-mail já está em uso. Não é possível criar uma nova conta com este e-mail.");
        }

        Role role = roleService.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: A função não foi encontrada"));

        UserRecord treatedUser = new UserRecord(null, userRecord.username(), userRecord.name(), userRecord.status(), userRecord.phoneNumber(), userRecord.cpf());

        User result = userService.saveUser(treatedUser, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * {@code GET  /user} : get all the Users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Users in body.
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers(@PageableDefault(size = 15) Pageable pageable) {
        log.info("REST request to get a pages of Users");

        Page<User> result = userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * {@code GET  /user/:id} : get the "id" User.
     *
     * @param id the id of the User to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the User, or with status {@code 400 (Bad Request)}.
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id")UUID id) {
        log.info("REST request to get User : {}", id);
        Optional<User> result = userService.findById(id);

        if(result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            throw new BadRequestException("Usuário não encontrado!");
        }
    }

    /**
     * {@code PUT  /user/password} : Finish to reset the password of the user.
     *
     * @param passwordRecord the generated key and the new password.
     * @throws BadRequestException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PutMapping("/user/password")
    public ResponseEntity<Object> createPassword(@RequestBody @Valid PasswordRecord passwordRecord) {
        if (passwordGenerator.isPasswordLengthInvalid(passwordRecord.password())) {
            throw new BadRequestException("Senha invalida!");
        }

        userService.completePasswordReset(passwordRecord);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Senha atualizada com sucesso"));
    }

    @PostMapping(value = "/user/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRecord resetPasswordRecord) {
        if (resetPasswordRecord.email() == null) {
            throw new BadRequestException("Campo email vazio");
        }

        if (!userService.checkUsername(resetPasswordRecord.email())) {
            throw new NotFoundException("Email não existe!");
        }

        userService.sendEmailToResetPassword(resetPasswordRecord);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Recriar a senha por email enviado com sucesso"));
    }

}
