package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.domain.User;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.dto.UserDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "clientAddress", source = "clientAddress", qualifiedByName = "clientAddressId")
    ClientDTO toDto(Client s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("clientAddressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientAddressDTO toDtoClientAddressId(ClientAddress clientAddress);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
