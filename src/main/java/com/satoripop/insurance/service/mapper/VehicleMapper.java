package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {}
