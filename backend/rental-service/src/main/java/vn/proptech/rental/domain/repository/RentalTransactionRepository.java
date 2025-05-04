package vn.proptech.rental.domain.repository;

import vn.proptech.rental.domain.model.RentalTransaction;
import vn.proptech.rental.domain.model.RentalTransactionStatus;

import java.time.Instant;
import java.util.List;

public interface RentalTransactionRepository {
    
    RentalTransaction findById(String id);
    List<RentalTransaction> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String propertyId,
        String listingId,
        String agentId,
        Instant startDate,
        Instant endDate,
        RentalTransactionStatus status
    );
    <S extends RentalTransaction> S save(S entity);
}