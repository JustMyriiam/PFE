package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.repository.InsurancePackRepository;
import com.satoripop.insurance.repository.search.InsurancePackSearchRepository;
import com.satoripop.insurance.service.InsurancePackService;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
import com.satoripop.insurance.service.mapper.InsurancePackMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.InsurancePack}.
 */
@Service
@Transactional
public class InsurancePackServiceImpl implements InsurancePackService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePackServiceImpl.class);

    private final InsurancePackRepository insurancePackRepository;

    private final InsurancePackMapper insurancePackMapper;

    private final InsurancePackSearchRepository insurancePackSearchRepository;

    public InsurancePackServiceImpl(
        InsurancePackRepository insurancePackRepository,
        InsurancePackMapper insurancePackMapper,
        InsurancePackSearchRepository insurancePackSearchRepository
    ) {
        this.insurancePackRepository = insurancePackRepository;
        this.insurancePackMapper = insurancePackMapper;
        this.insurancePackSearchRepository = insurancePackSearchRepository;
    }

    @Override
    public InsurancePackDTO save(InsurancePackDTO insurancePackDTO) {
        LOG.debug("Request to save InsurancePack : {}", insurancePackDTO);
        InsurancePack insurancePack = insurancePackMapper.toEntity(insurancePackDTO);
        insurancePack = insurancePackRepository.save(insurancePack);
        insurancePackSearchRepository.index(insurancePack);
        return insurancePackMapper.toDto(insurancePack);
    }

    @Override
    public InsurancePackDTO update(InsurancePackDTO insurancePackDTO) {
        LOG.debug("Request to update InsurancePack : {}", insurancePackDTO);
        InsurancePack insurancePack = insurancePackMapper.toEntity(insurancePackDTO);
        insurancePack = insurancePackRepository.save(insurancePack);
        insurancePackSearchRepository.index(insurancePack);
        return insurancePackMapper.toDto(insurancePack);
    }

    @Override
    public Optional<InsurancePackDTO> partialUpdate(InsurancePackDTO insurancePackDTO) {
        LOG.debug("Request to partially update InsurancePack : {}", insurancePackDTO);

        return insurancePackRepository
            .findById(insurancePackDTO.getId())
            .map(existingInsurancePack -> {
                insurancePackMapper.partialUpdate(existingInsurancePack, insurancePackDTO);

                return existingInsurancePack;
            })
            .map(insurancePackRepository::save)
            .map(savedInsurancePack -> {
                insurancePackSearchRepository.index(savedInsurancePack);
                return savedInsurancePack;
            })
            .map(insurancePackMapper::toDto);
    }

    public Page<InsurancePackDTO> findAllWithEagerRelationships(Pageable pageable) {
        return insurancePackRepository.findAllWithEagerRelationships(pageable).map(insurancePackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsurancePackDTO> findOne(UUID id) {
        LOG.debug("Request to get InsurancePack : {}", id);
        return insurancePackRepository.findOneWithEagerRelationships(id).map(insurancePackMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete InsurancePack : {}", id);
        insurancePackRepository.deleteById(id);
        insurancePackSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsurancePackDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of InsurancePacks for query {}", query);
        return insurancePackSearchRepository.search(query, pageable).map(insurancePackMapper::toDto);
    }
}
