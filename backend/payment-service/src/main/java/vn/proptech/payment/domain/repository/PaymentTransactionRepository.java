package vn.proptech.payment.domain.repository;


import org.springframework.stereotype.Repository;
import vn.proptech.payment.domain.model.PaymentTransactionStatus;
import vn.proptech.payment.domain.model.PaymentTransactionType;
import vn.proptech.payment.domain.model.PaymentTransaction;
import java.time.Instant;
import java.util.List;

@Repository
public interface PaymentTransactionRepository {
    
    PaymentTransaction findById(String id);
    List<PaymentTransaction> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String walletId,
        Instant startDate,
        Instant endDate,
        PaymentTransactionStatus status,
        PaymentTransactionType type
    );
    <S extends PaymentTransaction> S save(S entity);
}