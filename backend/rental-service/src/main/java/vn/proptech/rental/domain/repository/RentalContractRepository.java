package vn.proptech.rental.domain.repository;

import vn.proptech.rental.domain.model.RentalContract;

import java.time.Instant;
import java.util.List;

public interface RentalContractRepository{
    RentalContract findById(String id);
    
    List<RentalContract> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String transactionId,
        String uploadedBy,
        Instant startDate,
        Instant endDate
        );
    
        <S extends RentalContract> S save(S entity);
}