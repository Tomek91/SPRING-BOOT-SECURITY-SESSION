package pl.com.app.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.MyException;
import pl.com.app.model.Role;
import pl.com.app.model.User;
import pl.com.app.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUserName(username)
                .orElseThrow(() -> new MyException(ExceptionCode.SERVICE, "USERNAME NOT FOUND"));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getActive(), true, true, true,
                getRoles(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getRoles(Role role) {
        return Stream.of(role).map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
    }


}
