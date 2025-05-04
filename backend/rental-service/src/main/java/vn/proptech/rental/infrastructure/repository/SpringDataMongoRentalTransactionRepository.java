package vn.proptech.rental.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.rental.domain.model.RentalTransaction;

public interface SpringDataMongoRentalTransactionRepository extends MongoRepository<RentalTransaction, String> {

}
