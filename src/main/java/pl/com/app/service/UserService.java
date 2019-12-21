package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.UserDTO;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.MyException;
import pl.com.app.mapper.UserMapper;
import pl.com.app.model.User;
import pl.com.app.repository.RoleRepository;
import pl.com.app.repository.UserRepository;

import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO saveUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new MyException(ExceptionCode.SERVICE, "user is null");
        }
        User userToSave = userMapper.userDTOToUser(userDTO);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        Stream.of(Boolean.TRUE)
                .filter(x -> userToSave.getRole() != null)
                .filter(x -> userToSave.getRole().getName() != null)
                .flatMap(x -> roleRepository.findByName(userToSave.getRole().getName()).stream())
                .findFirst()
                .ifPresent(userToSave::setRole);

        User userFromDb = userRepository.save(userToSave);
        return userMapper.userToUserDTO(userFromDb);

    }
}
