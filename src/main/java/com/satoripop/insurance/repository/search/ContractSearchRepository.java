package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.repository.ContractRepository;
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
 * Spring Data Elasticsearch repository for the {@link Contract} entity.
 */
public interface ContractSearchRepository extends ElasticsearchRepository<Contract, UUID>, ContractSearchRepositoryInternal {}

interface ContractSearchRepositoryInternal {
    Page<Contract> search(String query, Pageable pageable);

    Page<Contract> search(Query query);

    @Async
    void index(Contract entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class ContractSearchRepositoryInternalImpl implements ContractSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ContractRepository repository;

    ContractSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ContractRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Contract> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Contract> search(Query query) {
        SearchHits<Contract> searchHits = elasticsearchTemplate.search(query, Contract.class);
        List<Contract> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Contract entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Contract.class);
    }
}
