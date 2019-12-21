package pl.com.app.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.exception.ExceptionMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessage<T> {
    private T data;
    private ExceptionMessage exceptionMessage;
}
