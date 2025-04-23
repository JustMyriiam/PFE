package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.repository.WarrantyRepository;
import com.satoripop.insurance.repository.search.WarrantySearchRepository;
import com.satoripop.insurance.service.WarrantyService;
import com.satoripop.insurance.service.dto.WarrantyDTO;
import com.satoripop.insurance.service.mapper.WarrantyMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Warranty}.
 */
@Service
@Transactional
public class WarrantyServiceImpl implements WarrantyService {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyServiceImpl.class);

    private final WarrantyRepository warrantyRepository;

    private final WarrantyMapper warrantyMapper;

    private final WarrantySearchRepository warrantySearchRepository;

    public WarrantyServiceImpl(
        WarrantyRepository warrantyRepository,
        WarrantyMapper warrantyMapper,
        WarrantySearchRepository warrantySearchRepository
    ) {
        this.warrantyRepository = warrantyRepository;
        this.warrantyMapper = warrantyMapper;
        this.warrantySearchRepository = warrantySearchRepository;
    }

    @Override
    public WarrantyDTO save(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to save Warranty : {}", warrantyDTO);
        Warranty warranty = warrantyMapper.toEntity(warrantyDTO);
        warranty = warrantyRepository.save(warranty);
        warrantySearchRepository.index(warranty);
        return warrantyMapper.toDto(warranty);
    }

    @Override
    public WarrantyDTO update(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to update Warranty : {}", warrantyDTO);
        Warranty warranty = warrantyMapper.toEntity(warrantyDTO);
        warranty = warrantyRepository.save(warranty);
        warrantySearchRepository.index(warranty);
        return warrantyMapper.toDto(warranty);
    }

    @Override
    public Optional<WarrantyDTO> partialUpdate(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to partially update Warranty : {}", warrantyDTO);

        return warrantyRepository
            .findById(warrantyDTO.getId())
            .map(existingWarranty -> {
                warrantyMapper.partialUpdate(existingWarranty, warrantyDTO);

                return existingWarranty;
            })
            .map(warrantyRepository::save)
            .map(savedWarranty -> {
                warrantySearchRepository.index(savedWarranty);
                return savedWarranty;
            })
            .map(warrantyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WarrantyDTO> findOne(UUID id) {
        LOG.debug("Request to get Warranty : {}", id);
        return warrantyRepository.findById(id).map(warrantyMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Warranty : {}", id);
        warrantyRepository.deleteById(id);
        warrantySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarrantyDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Warranties for query {}", query);
        return warrantySearchRepository.search(query, pageable).map(warrantyMapper::toDto);
    }
}
