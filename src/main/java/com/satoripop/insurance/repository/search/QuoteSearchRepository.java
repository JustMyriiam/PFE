package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Quote;
import com.satoripop.insurance.repository.QuoteRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Quote} entity.
 */
public interface QuoteSearchRepository extends ElasticsearchRepository<Quote, UUID>, QuoteSearchRepositoryInternal {}

interface QuoteSearchRepositoryInternal {
    Page<Quote> search(String query, Pageable pageable);

    Page<Quote> search(Query query);

    @Async
    void index(Quote entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class QuoteSearchRepositoryInternalImpl implements QuoteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final QuoteRepository repository;

    QuoteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, QuoteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Quote> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Quote> search(Query query) {
        SearchHits<Quote> searchHits = elasticsearchTemplate.search(query, Quote.class);
        List<Quote> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Quote entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Quote.class);
    }
}
