package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Quote;
import com.satoripop.insurance.repository.QuoteRepository;
import com.satoripop.insurance.repository.search.QuoteSearchRepository;
import com.satoripop.insurance.service.QuoteService;
import com.satoripop.insurance.service.dto.QuoteDTO;
import com.satoripop.insurance.service.mapper.QuoteMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Quote}.
 */
@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);

    private final QuoteRepository quoteRepository;

    private final QuoteMapper quoteMapper;

    private final QuoteSearchRepository quoteSearchRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository, QuoteMapper quoteMapper, QuoteSearchRepository quoteSearchRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.quoteSearchRepository = quoteSearchRepository;
    }

    @Override
    public QuoteDTO save(QuoteDTO quoteDTO) {
        LOG.debug("Request to save Quote : {}", quoteDTO);
        Quote quote = quoteMapper.toEntity(quoteDTO);
        quote = quoteRepository.save(quote);
        quoteSearchRepository.index(quote);
        return quoteMapper.toDto(quote);
    }

    @Override
    public QuoteDTO update(QuoteDTO quoteDTO) {
        LOG.debug("Request to update Quote : {}", quoteDTO);
        Quote quote = quoteMapper.toEntity(quoteDTO);
        quote = quoteRepository.save(quote);
        quoteSearchRepository.index(quote);
        return quoteMapper.toDto(quote);
    }

    @Override
    public Optional<QuoteDTO> partialUpdate(QuoteDTO quoteDTO) {
        LOG.debug("Request to partially update Quote : {}", quoteDTO);

        return quoteRepository
            .findById(quoteDTO.getId())
            .map(existingQuote -> {
                quoteMapper.partialUpdate(existingQuote, quoteDTO);

                return existingQuote;
            })
            .map(quoteRepository::save)
            .map(savedQuote -> {
                quoteSearchRepository.index(savedQuote);
                return savedQuote;
            })
            .map(quoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuoteDTO> findOne(UUID id) {
        LOG.debug("Request to get Quote : {}", id);
        return quoteRepository.findById(id).map(quoteMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Quote : {}", id);
        quoteRepository.deleteById(id);
        quoteSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuoteDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Quotes for query {}", query);
        return quoteSearchRepository.search(query, pageable).map(quoteMapper::toDto);
    }
}
