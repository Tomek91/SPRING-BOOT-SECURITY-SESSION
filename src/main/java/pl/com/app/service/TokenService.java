package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.TokenDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.MyException;
import pl.com.app.mapper.TokenMapper;
import pl.com.app.mapper.UserMapper;
import pl.com.app.model.Token;
import pl.com.app.model.User;
import pl.com.app.repository.TokenRepository;
import pl.com.app.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;

    public TokenDTO addToken(String token, String username){
        if (token == null){
            throw new MyException(ExceptionCode.SERVICE, "token is null");
        }
        if (username == null){
            throw new MyException(ExceptionCode.SERVICE, "username is null");
        }
        User user = userRepository.findByUserName(username).orElseThrow(() -> new MyException(ExceptionCode.SERVICE, "user is null"));
        Token tokenToSave = Token.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusHours(12))
                .build();

        Token tokenFromDb = tokenRepository.save(tokenToSave);
        return tokenMapper.tokenToTokenDTO(tokenFromDb);
    }

    public TokenDTO deleteToken(String token){
        if (token == null){
            throw new MyException(ExceptionCode.SERVICE, "token is null");
        }

        Token tokenToDelete = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new MyException(ExceptionCode.SERVICE, "token is null"));

        SecurityContextHolder.getContext().setAuthentication(null );
        tokenRepository.delete(tokenToDelete);
        return tokenMapper.tokenToTokenDTO(tokenToDelete);
    }

    public TokenDTO verifyingToken(String token){
        if (token == null){
            throw new MyException(ExceptionCode.SERVICE, "token is null");
        }

        Token tokenFromDb = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new MyException(ExceptionCode.SERVICE, "token is null"));

        return tokenMapper.tokenToTokenDTO(tokenFromDb);
    }

    public UserDTO verifyAndGetUser(String token) {
        if (token == null){
            throw new MyException(ExceptionCode.SERVICE, "token is null");
        }
        TokenDTO tokenDTO = verifyingToken(token);
        User user = userRepository.getOne(tokenDTO.getUserDTO().getId());
        return userMapper.userToUserDTO(user);
    }
}
