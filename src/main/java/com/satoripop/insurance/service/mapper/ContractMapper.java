package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.service.dto.AgencyDTO;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.dto.ContractDTO;
import com.satoripop.insurance.service.dto.VehicleDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "agency", source = "agency", qualifiedByName = "agencyId")
    ContractDTO toDto(Contract s);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("agencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgencyDTO toDtoAgencyId(Agency agency);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
