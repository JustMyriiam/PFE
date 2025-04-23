package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.repository.ClientRepository;
import com.satoripop.insurance.repository.search.ClientSearchRepository;
import com.satoripop.insurance.service.ClientService;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.mapper.ClientMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final ClientSearchRepository clientSearchRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientSearchRepository = clientSearchRepository;
    }

    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        LOG.debug("Request to save Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        clientSearchRepository.index(client);
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDTO update(ClientDTO clientDTO) {
        LOG.debug("Request to update Client : {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        clientSearchRepository.index(client);
        return clientMapper.toDto(client);
    }

    @Override
    public Optional<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        LOG.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(existingClient -> {
                clientMapper.partialUpdate(existingClient, clientDTO);

                return existingClient;
            })
            .map(clientRepository::save)
            .map(savedClient -> {
                clientSearchRepository.index(savedClient);
                return savedClient;
            })
            .map(clientMapper::toDto);
    }

    public Page<ClientDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clientRepository.findAllWithEagerRelationships(pageable).map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(UUID id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findOneWithEagerRelationships(id).map(clientMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
        clientSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Clients for query {}", query);
        return clientSearchRepository.search(query, pageable).map(clientMapper::toDto);
    }
}
