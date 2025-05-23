package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Client;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Client entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    default Optional<Client> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Client> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Client> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select client from Client client left join fetch client.user", countQuery = "select count(client) from Client client")
    Page<Client> findAllWithToOneRelationships(Pageable pageable);

    @Query("select client from Client client left join fetch client.user")
    List<Client> findAllWithToOneRelationships();

    @Query("select client from Client client left join fetch client.user where client.id =:id")
    Optional<Client> findOneWithToOneRelationships(@Param("id") UUID id);
}
