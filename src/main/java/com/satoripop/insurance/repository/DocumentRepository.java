package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Document;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {}
