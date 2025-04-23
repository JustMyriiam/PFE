package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Quote;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID>, JpaSpecificationExecutor<Quote> {}
