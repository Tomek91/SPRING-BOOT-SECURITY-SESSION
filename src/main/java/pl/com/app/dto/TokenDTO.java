package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenDTO {
    private Long id;
    private String token;
    private LocalDateTime expirationDate;
    private UserDTO userDTO;
}
