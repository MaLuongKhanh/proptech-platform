package vn.proptech.listing.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.listing.domain.model.Listing;

public interface SpringDataMongoListingRepository extends MongoRepository<Listing, String> {
    
    Page<Listing> findByAgentId(String agentId, Pageable pageable);
}