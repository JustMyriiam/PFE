package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Governorate} and its DTO {@link GovernorateDTO}.
 */
@Mapper(componentModel = "spring")
public interface GovernorateMapper extends EntityMapper<GovernorateDTO, Governorate> {}
