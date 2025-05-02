package vn.proptech.sale.domain.repository;

import org.springframework.stereotype.Repository;

import vn.proptech.sale.domain.model.Transaction;
import vn.proptech.sale.domain.model.TransactionStatus;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepository {
    Transaction findById(String id);
    List<Transaction> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String propertyId,
        String listingId,
        String agentId,
        Instant startDate,
        Instant endDate,
        TransactionStatus status
    );
    <S extends Transaction> S save(S entity);
}