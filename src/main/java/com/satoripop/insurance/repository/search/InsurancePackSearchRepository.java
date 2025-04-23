package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.repository.InsurancePackRepository;
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
 * Spring Data Elasticsearch repository for the {@link InsurancePack} entity.
 */
public interface InsurancePackSearchRepository
    extends ElasticsearchRepository<InsurancePack, UUID>, InsurancePackSearchRepositoryInternal {}

interface InsurancePackSearchRepositoryInternal {
    Page<InsurancePack> search(String query, Pageable pageable);

    Page<InsurancePack> search(Query query);

    @Async
    void index(InsurancePack entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class InsurancePackSearchRepositoryInternalImpl implements InsurancePackSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InsurancePackRepository repository;

    InsurancePackSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, InsurancePackRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InsurancePack> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InsurancePack> search(Query query) {
        SearchHits<InsurancePack> searchHits = elasticsearchTemplate.search(query, InsurancePack.class);
        List<InsurancePack> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InsurancePack entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), InsurancePack.class);
    }
}
