package br.com.financeiro.seguranca.config.security;

import br.com.financeiro.seguranca.domain.User;
import br.com.financeiro.seguranca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    public UserDetails loadUserByUserId(UUID userId) throws UsernameNotFoundException {
        User userModel = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        return UserDetailsImpl.build(userModel);
    }
}
