package pl.com.app.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exception.ExceptionCode;
import pl.com.app.exception.ExceptionInfo;
import pl.com.app.exception.ExceptionMessage;

@RestControllerAdvice
public class ExceptionsController {

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseMessage<String> accessDenied(WebRequest request) {
        return ResponseMessage.<String>builder().exceptionMessage(
                ExceptionMessage
                        .builder()
                        .exceptionInfo(new ExceptionInfo(ExceptionCode.ACCESS_DENIED, "ACCESS DENIED"))
                        .path(request.getDescription(false))
                        .build()).build();
    }


}
