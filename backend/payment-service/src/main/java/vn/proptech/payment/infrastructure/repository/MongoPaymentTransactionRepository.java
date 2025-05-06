package vn.proptech.payment.infrastructure.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import vn.proptech.payment.domain.model.PaymentTransaction;
import vn.proptech.payment.domain.model.PaymentTransactionStatus;
import vn.proptech.payment.domain.model.PaymentTransactionType;
import vn.proptech.payment.domain.repository.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoPaymentTransactionRepository implements PaymentTransactionRepository {
    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoPaymentTransactionRepository repository;

    @Override
    public PaymentTransaction findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        PaymentTransaction transaction = mongoTemplate.findOne(query, PaymentTransaction.class);
        return transaction;
    }

    @Override
    public List<PaymentTransaction> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String walletId,
        Instant startDate,
        Instant endDate,
        PaymentTransactionStatus status,
        PaymentTransactionType type
    ) {
        Query query = new Query(Criteria.where("isActive").is(true));
        if (walletId != null) {
            query.addCriteria(Criteria.where("walletId").is(walletId));
        }
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("startDate").lte(startDate));
            query.addCriteria(Criteria.where("endDate").gte(endDate));
        }

        int pageSize = limit != null ? limit : 10;
        int pageOffset = offset != null ? offset : 0;
        if (sort != null && direction != null) {
            Sort sortDirection = Sort.by(Sort.Direction.fromString(direction), sort);
            query.with(PageRequest.of(pageOffset, pageSize, sortDirection));
        } else {
            query.with(PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        }
        List<PaymentTransaction> transactions = mongoTemplate.find(query, PaymentTransaction.class);
        return transactions != null ? transactions : new ArrayList<>();
    }

    @Override
    public <S extends PaymentTransaction> S save(S entity) {
        return repository.save(entity);
    }
}
