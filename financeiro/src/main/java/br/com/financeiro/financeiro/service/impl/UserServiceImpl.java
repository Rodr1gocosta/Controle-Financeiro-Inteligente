package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.User;
import br.com.financeiro.financeiro.record.UserRecord;
import br.com.financeiro.financeiro.repository.UserRepository;
import br.com.financeiro.financeiro.service.UserService;
import br.com.financeiro.financeiro.service.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link User}.
 */
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserRecord save(UserRecord userRecord) {
        log.debug("Request to save User : {}", userRecord);
        User user = userMapper.toEntity(userRecord);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserRecord update(UserRecord userRecord) {
        log.debug("Request to update User : {}", userRecord);
        User user = userMapper.toEntity(userRecord);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public Optional<UserRecord> findOne(UUID id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}
