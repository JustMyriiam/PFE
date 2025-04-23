package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.service.dto.CityDTO;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClientAddress} and its DTO {@link ClientAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientAddressMapper extends EntityMapper<ClientAddressDTO, ClientAddress> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    ClientAddressDTO toDto(ClientAddress s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
