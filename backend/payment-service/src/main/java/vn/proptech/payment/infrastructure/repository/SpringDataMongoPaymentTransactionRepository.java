package vn.proptech.payment.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.proptech.payment.domain.model.PaymentTransaction;

public interface SpringDataMongoPaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

}
