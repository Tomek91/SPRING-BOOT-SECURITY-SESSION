package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String userName;
    private String password;
    private String passwordConfirmation;
    private String email;
    private Boolean active;
    private RoleDTO roleDTO;
}