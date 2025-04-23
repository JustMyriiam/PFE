package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Document;
import com.satoripop.insurance.repository.DocumentRepository;
import com.satoripop.insurance.repository.search.DocumentSearchRepository;
import com.satoripop.insurance.service.DocumentService;
import com.satoripop.insurance.service.dto.DocumentDTO;
import com.satoripop.insurance.service.mapper.DocumentMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Document}.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentSearchRepository documentSearchRepository;

    public DocumentServiceImpl(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        DocumentSearchRepository documentSearchRepository
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentSearchRepository = documentSearchRepository;
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        LOG.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        documentSearchRepository.index(document);
        return documentMapper.toDto(document);
    }

    @Override
    public DocumentDTO update(DocumentDTO documentDTO) {
        LOG.debug("Request to update Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        documentSearchRepository.index(document);
        return documentMapper.toDto(document);
    }

    @Override
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        LOG.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(existingDocument -> {
                documentMapper.partialUpdate(existingDocument, documentDTO);

                return existingDocument;
            })
            .map(documentRepository::save)
            .map(savedDocument -> {
                documentSearchRepository.index(savedDocument);
                return savedDocument;
            })
            .map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(UUID id) {
        LOG.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
        documentSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Documents for query {}", query);
        return documentSearchRepository.search(query, pageable).map(documentMapper::toDto);
    }
}
