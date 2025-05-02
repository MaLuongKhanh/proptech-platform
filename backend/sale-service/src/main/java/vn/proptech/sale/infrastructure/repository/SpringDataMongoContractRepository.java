package vn.proptech.sale.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.sale.domain.model.Contract;

public interface SpringDataMongoContractRepository extends MongoRepository<Contract, String> {
}