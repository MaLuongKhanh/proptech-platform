package vn.proptech.listing.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.listing.domain.model.Property;

public interface SpringDataMongoPropertyRepository extends MongoRepository<Property, String> {
}