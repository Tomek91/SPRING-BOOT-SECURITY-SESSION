package pl.com.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.TokenDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.model.Token;
import pl.com.app.model.User;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    @Mappings({
            @Mapping(source = "user", target = "userDTO")
    })
    TokenDTO tokenToTokenDTO(Token token);

    @Mappings({
            @Mapping(source = "userDTO", target = "user")
    })
    Token tokenDTOToToken(TokenDTO tokenDTO);
}

