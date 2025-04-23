package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.repository.ClientAddressRepository;
import com.satoripop.insurance.repository.search.ClientAddressSearchRepository;
import com.satoripop.insurance.service.ClientAddressService;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
import com.satoripop.insurance.service.mapper.ClientAddressMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.ClientAddress}.
 */
@Service
@Transactional
public class ClientAddressServiceImpl implements ClientAddressService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientAddressServiceImpl.class);

    private final ClientAddressRepository clientAddressRepository;

    private final ClientAddressMapper clientAddressMapper;

    private final ClientAddressSearchRepository clientAddressSearchRepository;

    public ClientAddressServiceImpl(
        ClientAddressRepository clientAddressRepository,
        ClientAddressMapper clientAddressMapper,
        ClientAddressSearchRepository clientAddressSearchRepository
    ) {
        this.clientAddressRepository = clientAddressRepository;
        this.clientAddressMapper = clientAddressMapper;
        this.clientAddressSearchRepository = clientAddressSearchRepository;
    }

    @Override
    public ClientAddressDTO save(ClientAddressDTO clientAddressDTO) {
        LOG.debug("Request to save ClientAddress : {}", clientAddressDTO);
        ClientAddress clientAddress = clientAddressMapper.toEntity(clientAddressDTO);
        clientAddress = clientAddressRepository.save(clientAddress);
        clientAddressSearchRepository.index(clientAddress);
        return clientAddressMapper.toDto(clientAddress);
    }

    @Override
    public ClientAddressDTO update(ClientAddressDTO clientAddressDTO) {
        LOG.debug("Request to update ClientAddress : {}", clientAddressDTO);
        ClientAddress clientAddress = clientAddressMapper.toEntity(clientAddressDTO);
        clientAddress = clientAddressRepository.save(clientAddress);
        clientAddressSearchRepository.index(clientAddress);
        return clientAddressMapper.toDto(clientAddress);
    }

    @Override
    public Optional<ClientAddressDTO> partialUpdate(ClientAddressDTO clientAddressDTO) {
        LOG.debug("Request to partially update ClientAddress : {}", clientAddressDTO);

        return clientAddressRepository
            .findById(clientAddressDTO.getId())
            .map(existingClientAddress -> {
                clientAddressMapper.partialUpdate(existingClientAddress, clientAddressDTO);

                return existingClientAddress;
            })
            .map(clientAddressRepository::save)
            .map(savedClientAddress -> {
                clientAddressSearchRepository.index(savedClientAddress);
                return savedClientAddress;
            })
            .map(clientAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientAddressDTO> findOne(UUID id) {
        LOG.debug("Request to get ClientAddress : {}", id);
        return clientAddressRepository.findById(id).map(clientAddressMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete ClientAddress : {}", id);
        clientAddressRepository.deleteById(id);
        clientAddressSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientAddressDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ClientAddresses for query {}", query);
        return clientAddressSearchRepository.search(query, pageable).map(clientAddressMapper::toDto);
    }
}
