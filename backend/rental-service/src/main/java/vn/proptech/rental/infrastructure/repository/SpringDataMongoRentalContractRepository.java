package vn.proptech.rental.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.rental.domain.model.RentalContract;

public interface SpringDataMongoRentalContractRepository extends MongoRepository<RentalContract, String> {
}