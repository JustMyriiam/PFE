package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.service.dto.CityDTO;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "governorate", source = "governorate", qualifiedByName = "governorateId")
    CityDTO toDto(City s);

    @Named("governorateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GovernorateDTO toDtoGovernorateId(Governorate governorate);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
