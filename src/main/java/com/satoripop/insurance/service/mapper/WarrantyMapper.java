package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
import com.satoripop.insurance.service.dto.WarrantyDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warranty} and its DTO {@link WarrantyDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarrantyMapper extends EntityMapper<WarrantyDTO, Warranty> {
    @Mapping(target = "insurancePacks", source = "insurancePacks", qualifiedByName = "insurancePackIdSet")
    WarrantyDTO toDto(Warranty s);

    @Mapping(target = "insurancePacks", ignore = true)
    @Mapping(target = "removeInsurancePacks", ignore = true)
    Warranty toEntity(WarrantyDTO warrantyDTO);

    @Named("insurancePackId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InsurancePackDTO toDtoInsurancePackId(InsurancePack insurancePack);

    @Named("insurancePackIdSet")
    default Set<InsurancePackDTO> toDtoInsurancePackIdSet(Set<InsurancePack> insurancePack) {
        return insurancePack.stream().map(this::toDtoInsurancePackId).collect(Collectors.toSet());
    }

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
