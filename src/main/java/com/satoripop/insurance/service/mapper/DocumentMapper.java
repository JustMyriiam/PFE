package com.satoripop.insurance.service.mapper;

import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.domain.Document;
import com.satoripop.insurance.service.dto.ContractDTO;
import com.satoripop.insurance.service.dto.DocumentDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractId")
    DocumentDTO toDto(Document s);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoContractId(Contract contract);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
