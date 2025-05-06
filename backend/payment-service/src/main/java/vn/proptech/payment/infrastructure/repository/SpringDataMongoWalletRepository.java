package vn.proptech.payment.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.payment.domain.model.Wallet;

public interface SpringDataMongoWalletRepository extends MongoRepository<Wallet, String> {
}