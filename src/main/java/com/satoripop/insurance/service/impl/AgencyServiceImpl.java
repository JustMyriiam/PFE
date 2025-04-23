package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.repository.AgencyRepository;
import com.satoripop.insurance.repository.search.AgencySearchRepository;
import com.satoripop.insurance.service.AgencyService;
import com.satoripop.insurance.service.dto.AgencyDTO;
import com.satoripop.insurance.service.mapper.AgencyMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Agency}.
 */
@Service
@Transactional
public class AgencyServiceImpl implements AgencyService {

    private static final Logger LOG = LoggerFactory.getLogger(AgencyServiceImpl.class);

    private final AgencyRepository agencyRepository;

    private final AgencyMapper agencyMapper;

    private final AgencySearchRepository agencySearchRepository;

    public AgencyServiceImpl(AgencyRepository agencyRepository, AgencyMapper agencyMapper, AgencySearchRepository agencySearchRepository) {
        this.agencyRepository = agencyRepository;
        this.agencyMapper = agencyMapper;
        this.agencySearchRepository = agencySearchRepository;
    }

    @Override
    public AgencyDTO save(AgencyDTO agencyDTO) {
        LOG.debug("Request to save Agency : {}", agencyDTO);
        Agency agency = agencyMapper.toEntity(agencyDTO);
        agency = agencyRepository.save(agency);
        agencySearchRepository.index(agency);
        return agencyMapper.toDto(agency);
    }

    @Override
    public AgencyDTO update(AgencyDTO agencyDTO) {
        LOG.debug("Request to update Agency : {}", agencyDTO);
        Agency agency = agencyMapper.toEntity(agencyDTO);
        agency = agencyRepository.save(agency);
        agencySearchRepository.index(agency);
        return agencyMapper.toDto(agency);
    }

    @Override
    public Optional<AgencyDTO> partialUpdate(AgencyDTO agencyDTO) {
        LOG.debug("Request to partially update Agency : {}", agencyDTO);

        return agencyRepository
            .findById(agencyDTO.getId())
            .map(existingAgency -> {
                agencyMapper.partialUpdate(existingAgency, agencyDTO);

                return existingAgency;
            })
            .map(agencyRepository::save)
            .map(savedAgency -> {
                agencySearchRepository.index(savedAgency);
                return savedAgency;
            })
            .map(agencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyDTO> findOne(UUID id) {
        LOG.debug("Request to get Agency : {}", id);
        return agencyRepository.findById(id).map(agencyMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Agency : {}", id);
        agencyRepository.deleteById(id);
        agencySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Agencies for query {}", query);
        return agencySearchRepository.search(query, pageable).map(agencyMapper::toDto);
    }
}
