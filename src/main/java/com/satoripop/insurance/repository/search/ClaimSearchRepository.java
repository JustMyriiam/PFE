package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Claim;
import com.satoripop.insurance.repository.ClaimRepository;
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
 * Spring Data Elasticsearch repository for the {@link Claim} entity.
 */
public interface ClaimSearchRepository extends ElasticsearchRepository<Claim, UUID>, ClaimSearchRepositoryInternal {}

interface ClaimSearchRepositoryInternal {
    Page<Claim> search(String query, Pageable pageable);

    Page<Claim> search(Query query);

    @Async
    void index(Claim entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class ClaimSearchRepositoryInternalImpl implements ClaimSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ClaimRepository repository;

    ClaimSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ClaimRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Claim> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Claim> search(Query query) {
        SearchHits<Claim> searchHits = elasticsearchTemplate.search(query, Claim.class);
        List<Claim> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Claim entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Claim.class);
    }
}
