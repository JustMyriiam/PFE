package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.repository.ClientAddressRepository;
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
 * Spring Data Elasticsearch repository for the {@link ClientAddress} entity.
 */
public interface ClientAddressSearchRepository
    extends ElasticsearchRepository<ClientAddress, UUID>, ClientAddressSearchRepositoryInternal {}

interface ClientAddressSearchRepositoryInternal {
    Page<ClientAddress> search(String query, Pageable pageable);

    Page<ClientAddress> search(Query query);

    @Async
    void index(ClientAddress entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class ClientAddressSearchRepositoryInternalImpl implements ClientAddressSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ClientAddressRepository repository;

    ClientAddressSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ClientAddressRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ClientAddress> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ClientAddress> search(Query query) {
        SearchHits<ClientAddress> searchHits = elasticsearchTemplate.search(query, ClientAddress.class);
        List<ClientAddress> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ClientAddress entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), ClientAddress.class);
    }
}
