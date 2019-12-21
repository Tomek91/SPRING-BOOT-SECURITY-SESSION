package pl.com.app.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.ExceptionInfo;
import pl.com.app.exception.ExceptionMessage;
import pl.com.app.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    private final TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  TokenService tokenService,
                                  MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        super(authenticationManager);
        this.tokenService = tokenService;
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException {

        try {

            Optional<String> userSessionOpt = Stream.of(Boolean.TRUE)
                    .filter(x -> request.getCookies() != null)
                    .flatMap(x -> Arrays.stream(request.getCookies()))
                    .filter(x -> x.getName().equals(SecurityConfig.SESSION_PREFIX))
                    .map(Cookie::getValue)
                    .findFirst();


            if (userSessionOpt.isPresent()) {
                UserDTO userDTO = tokenService.verifyAndGetUser(userSessionOpt.get());
                if (userDTO != null) {
                    List<SimpleGrantedAuthority> roles =
                            Stream.of(userDTO.getRoleDTO().getName())
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());

                    SecurityContextHolder.getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(userDTO.getUserName(), null, roles)
                            );
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
            outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

            mappingJackson2HttpMessageConverter.write(ResponseMessage
                    .builder()
                    .exceptionMessage(
                            ExceptionMessage
                                    .builder()
                                    .exceptionInfo(new ExceptionInfo(ExceptionCode.SECURITY, e.getMessage()))
                                    .path(request.getContextPath())
                                    .build()
                    )
                    .build(), MediaType.APPLICATION_JSON_UTF8, outputMessage);
        }
    }
}
