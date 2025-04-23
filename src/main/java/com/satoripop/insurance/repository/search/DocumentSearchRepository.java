package com.satoripop.insurance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.satoripop.insurance.domain.Document;
import com.satoripop.insurance.repository.DocumentRepository;
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
 * Spring Data Elasticsearch repository for the {@link Document} entity.
 */
public interface DocumentSearchRepository extends ElasticsearchRepository<Document, UUID>, DocumentSearchRepositoryInternal {}

interface DocumentSearchRepositoryInternal {
    Page<Document> search(String query, Pageable pageable);

    Page<Document> search(Query query);

    @Async
    void index(Document entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class DocumentSearchRepositoryInternalImpl implements DocumentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentRepository repository;

    DocumentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Document> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Document> search(Query query) {
        SearchHits<Document> searchHits = elasticsearchTemplate.search(query, Document.class);
        List<Document> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Document entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Document.class);
    }
}
