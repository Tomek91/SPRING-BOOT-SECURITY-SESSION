package pl.com.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.UserDTO;
import pl.com.app.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(source = "role", target = "roleDTO")
    })
    UserDTO userToUserDTO(User user);

    @Mappings({
            @Mapping(source = "roleDTO", target = "role")
    })
    User userDTOToUser(UserDTO userDTO);
}

