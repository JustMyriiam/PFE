package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.service.dto.AgencyDTO;
import com.satoripop.insurance.service.dto.CityDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agency} and its DTO {@link AgencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgencyMapper extends EntityMapper<AgencyDTO, Agency> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    AgencyDTO toDto(Agency s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
