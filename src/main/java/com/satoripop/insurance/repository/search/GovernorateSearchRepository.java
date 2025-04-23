package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.repository.GovernorateRepository;
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
 * Spring Data Elasticsearch repository for the {@link Governorate} entity.
 */
public interface GovernorateSearchRepository extends ElasticsearchRepository<Governorate, UUID>, GovernorateSearchRepositoryInternal {}

interface GovernorateSearchRepositoryInternal {
    Page<Governorate> search(String query, Pageable pageable);

    Page<Governorate> search(Query query);

    @Async
    void index(Governorate entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class GovernorateSearchRepositoryInternalImpl implements GovernorateSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final GovernorateRepository repository;

    GovernorateSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, GovernorateRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Governorate> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Governorate> search(Query query) {
        SearchHits<Governorate> searchHits = elasticsearchTemplate.search(query, Governorate.class);
        List<Governorate> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Governorate entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Governorate.class);
    }
}
