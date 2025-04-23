package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.repository.ClientRepository;
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
 * Spring Data Elasticsearch repository for the {@link Client} entity.
 */
public interface ClientSearchRepository extends ElasticsearchRepository<Client, UUID>, ClientSearchRepositoryInternal {}

interface ClientSearchRepositoryInternal {
    Page<Client> search(String query, Pageable pageable);

    Page<Client> search(Query query);

    @Async
    void index(Client entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class ClientSearchRepositoryInternalImpl implements ClientSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ClientRepository repository;

    ClientSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ClientRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Client> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Client> search(Query query) {
        SearchHits<Client> searchHits = elasticsearchTemplate.search(query, Client.class);
        List<Client> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Client entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Client.class);
    }
}
