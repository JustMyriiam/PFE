package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.repository.WarrantyRepository;
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
 * Spring Data Elasticsearch repository for the {@link Warranty} entity.
 */
public interface WarrantySearchRepository extends ElasticsearchRepository<Warranty, UUID>, WarrantySearchRepositoryInternal {}

interface WarrantySearchRepositoryInternal {
    Page<Warranty> search(String query, Pageable pageable);

    Page<Warranty> search(Query query);

    @Async
    void index(Warranty entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class WarrantySearchRepositoryInternalImpl implements WarrantySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WarrantyRepository repository;

    WarrantySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WarrantyRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Warranty> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Warranty> search(Query query) {
        SearchHits<Warranty> searchHits = elasticsearchTemplate.search(query, Warranty.class);
        List<Warranty> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Warranty entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Warranty.class);
    }
}
