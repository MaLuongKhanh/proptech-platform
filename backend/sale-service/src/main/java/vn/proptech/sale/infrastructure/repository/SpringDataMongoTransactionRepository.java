package vn.proptech.sale.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.sale.domain.model.Transaction;

public interface SpringDataMongoTransactionRepository extends MongoRepository<Transaction, String> {

}
