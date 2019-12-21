package pl.com.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.com.app.dto.rest.ResponseMessage;


@RestControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(MyException.class)
    public final ResponseEntity<ResponseMessage> handleExceptions(MyException ex, WebRequest request) {

        return new ResponseEntity<>(
                ResponseMessage
                        .builder()
                        .exceptionMessage(
                                ExceptionMessage
                                        .builder()
                                        .exceptionInfo(ex.getExceptionInfo())
                                        .path(request.getDescription(false))
                                        .build()
                        )
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                ResponseMessage
                        .builder()
                        .exceptionMessage(
                                ExceptionMessage
                                        .builder()
                                        .exceptionInfo(new ExceptionInfo(ExceptionCode.NOT_FOUND, "CUSTOM NOT FOUND"))
                                        .path(request.getDescription(false))
                                        .build()
                        )
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleAllExceptions(ex, request);
    }

    @ExceptionHandler( Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        return new ResponseEntity<>(
                ResponseMessage
                        .builder()
                        .exceptionMessage(
                                ExceptionMessage
                                        .builder()
                                        .exceptionInfo(new ExceptionInfo(ExceptionCode.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()))
                                        .path(request.getDescription(false))
                                        .build()
                        )
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

