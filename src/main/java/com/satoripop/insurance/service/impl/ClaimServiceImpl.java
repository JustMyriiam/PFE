package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Claim;
import com.satoripop.insurance.repository.ClaimRepository;
import com.satoripop.insurance.repository.search.ClaimSearchRepository;
import com.satoripop.insurance.service.ClaimService;
import com.satoripop.insurance.service.dto.ClaimDTO;
import com.satoripop.insurance.service.mapper.ClaimMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Claim}.
 */
@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimServiceImpl.class);

    private final ClaimRepository claimRepository;

    private final ClaimMapper claimMapper;

    private final ClaimSearchRepository claimSearchRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, ClaimMapper claimMapper, ClaimSearchRepository claimSearchRepository) {
        this.claimRepository = claimRepository;
        this.claimMapper = claimMapper;
        this.claimSearchRepository = claimSearchRepository;
    }

    @Override
    public ClaimDTO save(ClaimDTO claimDTO) {
        LOG.debug("Request to save Claim : {}", claimDTO);
        Claim claim = claimMapper.toEntity(claimDTO);
        claim = claimRepository.save(claim);
        claimSearchRepository.index(claim);
        return claimMapper.toDto(claim);
    }

    @Override
    public ClaimDTO update(ClaimDTO claimDTO) {
        LOG.debug("Request to update Claim : {}", claimDTO);
        Claim claim = claimMapper.toEntity(claimDTO);
        claim = claimRepository.save(claim);
        claimSearchRepository.index(claim);
        return claimMapper.toDto(claim);
    }

    @Override
    public Optional<ClaimDTO> partialUpdate(ClaimDTO claimDTO) {
        LOG.debug("Request to partially update Claim : {}", claimDTO);

        return claimRepository
            .findById(claimDTO.getId())
            .map(existingClaim -> {
                claimMapper.partialUpdate(existingClaim, claimDTO);

                return existingClaim;
            })
            .map(claimRepository::save)
            .map(savedClaim -> {
                claimSearchRepository.index(savedClaim);
                return savedClaim;
            })
            .map(claimMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClaimDTO> findOne(UUID id) {
        LOG.debug("Request to get Claim : {}", id);
        return claimRepository.findById(id).map(claimMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Claim : {}", id);
        claimRepository.deleteById(id);
        claimSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Claims for query {}", query);
        return claimSearchRepository.search(query, pageable).map(claimMapper::toDto);
    }
}
