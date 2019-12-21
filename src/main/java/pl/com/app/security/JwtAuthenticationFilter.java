package pl.com.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pl.com.app.dto.AuthenticationUserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.ExceptionInfo;
import pl.com.app.exception.ExceptionMessage;
import pl.com.app.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static pl.com.app.security.SecurityConfig.SESSION_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        try {

            AuthenticationUserDTO user
                    = new ObjectMapper().readValue(request.getInputStream(), AuthenticationUserDTO.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()));

        } catch (Exception e) {
            try {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(
                        new ObjectMapper().writeValueAsString(
                                ResponseMessage
                                        .builder()
                                        .exceptionMessage(
                                                ExceptionMessage
                                                        .builder()
                                                        .exceptionInfo(new ExceptionInfo(ExceptionCode.SECURITY, "attempt authentication: " + e.getMessage()))
                                                        .path(request.getContextPath())
                                                        .build()
                                        )
                                        .build()
                        )
                );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String token = UUID.randomUUID().toString();
        tokenService.addToken(token, authResult.getName());
        Cookie userSession = new Cookie(SESSION_PREFIX, token);
        userSession.setHttpOnly(true);
        userSession.setMaxAge(SecurityConfig.COOKIE_EXPIRATION_TIME);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(userSession);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        ResponseMessage
                                .<String>builder()
                                .data("Ok user session")
                                .build()
                )
        );
        response.getWriter().flush();
        response.getWriter().close();

    }


}
