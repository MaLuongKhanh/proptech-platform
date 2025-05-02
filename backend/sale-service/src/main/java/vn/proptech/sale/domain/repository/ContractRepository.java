package vn.proptech.sale.domain.repository;

import java.time.Instant;
import java.util.List;

import vn.proptech.sale.domain.model.Contract;

public interface ContractRepository {
    Contract findById(String id);
    List<Contract> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String transactionId,
        String uploadedBy,
        Instant startDate,
        Instant endDate
    );
    <S extends Contract> S save(S entity);
}