package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Claim;
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.service.dto.ClaimDTO;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.dto.ContractDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Claim} and its DTO {@link ClaimDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClaimMapper extends EntityMapper<ClaimDTO, Claim> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractId")
    ClaimDTO toDto(Claim s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoContractId(Contract contract);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
