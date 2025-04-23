package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.repository.GovernorateRepository;
import com.satoripop.insurance.repository.search.GovernorateSearchRepository;
import com.satoripop.insurance.service.GovernorateService;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import com.satoripop.insurance.service.mapper.GovernorateMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Governorate}.
 */
@Service
@Transactional
public class GovernorateServiceImpl implements GovernorateService {

    private static final Logger LOG = LoggerFactory.getLogger(GovernorateServiceImpl.class);

    private final GovernorateRepository governorateRepository;

    private final GovernorateMapper governorateMapper;

    private final GovernorateSearchRepository governorateSearchRepository;

    public GovernorateServiceImpl(
        GovernorateRepository governorateRepository,
        GovernorateMapper governorateMapper,
        GovernorateSearchRepository governorateSearchRepository
    ) {
        this.governorateRepository = governorateRepository;
        this.governorateMapper = governorateMapper;
        this.governorateSearchRepository = governorateSearchRepository;
    }

    @Override
    public GovernorateDTO save(GovernorateDTO governorateDTO) {
        LOG.debug("Request to save Governorate : {}", governorateDTO);
        Governorate governorate = governorateMapper.toEntity(governorateDTO);
        governorate = governorateRepository.save(governorate);
        governorateSearchRepository.index(governorate);
        return governorateMapper.toDto(governorate);
    }

    @Override
    public GovernorateDTO update(GovernorateDTO governorateDTO) {
        LOG.debug("Request to update Governorate : {}", governorateDTO);
        Governorate governorate = governorateMapper.toEntity(governorateDTO);
        governorate = governorateRepository.save(governorate);
        governorateSearchRepository.index(governorate);
        return governorateMapper.toDto(governorate);
    }

    @Override
    public Optional<GovernorateDTO> partialUpdate(GovernorateDTO governorateDTO) {
        LOG.debug("Request to partially update Governorate : {}", governorateDTO);

        return governorateRepository
            .findById(governorateDTO.getId())
            .map(existingGovernorate -> {
                governorateMapper.partialUpdate(existingGovernorate, governorateDTO);

                return existingGovernorate;
            })
            .map(governorateRepository::save)
            .map(savedGovernorate -> {
                governorateSearchRepository.index(savedGovernorate);
                return savedGovernorate;
            })
            .map(governorateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GovernorateDTO> findOne(UUID id) {
        LOG.debug("Request to get Governorate : {}", id);
        return governorateRepository.findById(id).map(governorateMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Governorate : {}", id);
        governorateRepository.deleteById(id);
        governorateSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GovernorateDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Governorates for query {}", query);
        return governorateSearchRepository.search(query, pageable).map(governorateMapper::toDto);
    }
}
