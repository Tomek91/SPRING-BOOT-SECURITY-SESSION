package pl.com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.ExceptionInfo;
import pl.com.app.exception.ExceptionMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPointFilter implements AuthenticationEntryPoint {

    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException, ServletException {

        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

        messageConverter.write(ResponseMessage
                .builder()
                .exceptionMessage(
                        ExceptionMessage
                                .builder()
                                .exceptionInfo(new ExceptionInfo(ExceptionCode.SECURITY, e.getMessage()))
                                .path(httpServletRequest.getContextPath())
                                .build()
                )
                .build(), MediaType.APPLICATION_JSON_UTF8, outputMessage);

    }
}
