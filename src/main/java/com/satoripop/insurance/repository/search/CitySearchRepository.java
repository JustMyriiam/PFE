package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.repository.CityRepository;
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
 * Spring Data Elasticsearch repository for the {@link City} entity.
 */
public interface CitySearchRepository extends ElasticsearchRepository<City, UUID>, CitySearchRepositoryInternal {}

interface CitySearchRepositoryInternal {
    Page<City> search(String query, Pageable pageable);

    Page<City> search(Query query);

    @Async
    void index(City entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class CitySearchRepositoryInternalImpl implements CitySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CityRepository repository;

    CitySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CityRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<City> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<City> search(Query query) {
        SearchHits<City> searchHits = elasticsearchTemplate.search(query, City.class);
        List<City> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(City entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), City.class);
    }
}
