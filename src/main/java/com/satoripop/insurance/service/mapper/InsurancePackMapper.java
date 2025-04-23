package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
import com.satoripop.insurance.service.dto.VehicleDTO;
import com.satoripop.insurance.service.dto.WarrantyDTO;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InsurancePack} and its DTO {@link InsurancePackDTO}.
 */
@Mapper(componentModel = "spring")
public interface InsurancePackMapper extends EntityMapper<InsurancePackDTO, InsurancePack> {
    @Mapping(target = "warranties", source = "warranties", qualifiedByName = "warrantyIdSet")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    InsurancePackDTO toDto(InsurancePack s);

    @Mapping(target = "removeWarranties", ignore = true)
    InsurancePack toEntity(InsurancePackDTO insurancePackDTO);

    @Named("warrantyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarrantyDTO toDtoWarrantyId(Warranty warranty);

    @Named("warrantyIdSet")
    default Set<WarrantyDTO> toDtoWarrantyIdSet(Set<Warranty> warranty) {
        return warranty.stream().map(this::toDtoWarrantyId).collect(Collectors.toSet());
    }

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
