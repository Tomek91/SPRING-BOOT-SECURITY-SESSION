package pl.com.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.TokenDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.TokenService;
import pl.com.app.service.UserService;

import static pl.com.app.security.SecurityConfig.SESSION_PREFIX;


@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class SecurityController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<UserDTO>> addUser(@RequestBody UserDTO user){
        return ResponseEntity.ok(ResponseMessage.<UserDTO>builder().data(userService.saveUser(user)).build());
    }

    @PostMapping("/log-out")
    public ResponseEntity<ResponseMessage<TokenDTO>> logOut(
            @CookieValue(value = SESSION_PREFIX) String token){
        return ResponseEntity.ok(ResponseMessage.<TokenDTO>builder().data(tokenService.deleteToken(token)).build());
    }
}
