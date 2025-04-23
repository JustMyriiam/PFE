package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Quote;
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.dto.QuoteDTO;
import com.satoripop.insurance.service.dto.VehicleDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quote} and its DTO {@link QuoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuoteMapper extends EntityMapper<QuoteDTO, Quote> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    QuoteDTO toDto(Quote s);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
