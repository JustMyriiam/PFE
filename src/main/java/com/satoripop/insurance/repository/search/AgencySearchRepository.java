package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.repository.AgencyRepository;
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
 * Spring Data Elasticsearch repository for the {@link Agency} entity.
 */
public interface AgencySearchRepository extends ElasticsearchRepository<Agency, UUID>, AgencySearchRepositoryInternal {}

interface AgencySearchRepositoryInternal {
    Page<Agency> search(String query, Pageable pageable);

    Page<Agency> search(Query query);

    @Async
    void index(Agency entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class AgencySearchRepositoryInternalImpl implements AgencySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AgencyRepository repository;

    AgencySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AgencyRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Agency> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Agency> search(Query query) {
        SearchHits<Agency> searchHits = elasticsearchTemplate.search(query, Agency.class);
        List<Agency> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Agency entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Agency.class);
    }
}
