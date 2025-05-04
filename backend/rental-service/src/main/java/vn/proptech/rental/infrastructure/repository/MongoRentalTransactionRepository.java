package vn.proptech.rental.infrastructure.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import vn.proptech.rental.domain.model.RentalTransaction;
import vn.proptech.rental.domain.model.RentalTransactionStatus;
import vn.proptech.rental.domain.repository.RentalTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoRentalTransactionRepository implements RentalTransactionRepository {
    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoRentalTransactionRepository repository;

    @Override
    public RentalTransaction findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        RentalTransaction transaction = mongoTemplate.findOne(query, RentalTransaction.class);
        return transaction;
    }

    @Override
    public List<RentalTransaction> findAll(
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
    ) {
        Query query = new Query(Criteria.where("isActive").is(true));
        if (propertyId != null) {
            query.addCriteria(Criteria.where("propertyId").is(propertyId));
        }
        if (listingId != null) {
            query.addCriteria(Criteria.where("listingId").is(listingId));
        }
        if (agentId != null) {
            query.addCriteria(Criteria.where("agentId").is(agentId));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("startDate").lte(startDate));
            query.addCriteria(Criteria.where("endDate").gte(endDate));
        }
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        int pageSize = limit != null ? limit : 10;
        int pageOffset = offset != null ? offset : 0;
        if (sort != null && direction != null) {
            Sort sortDirection = Sort.by(Sort.Direction.fromString(direction), sort);
            query.with(PageRequest.of(pageOffset, pageSize, sortDirection));
        } else {
            query.with(PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        }
        List<RentalTransaction> transactions = mongoTemplate.find(query, RentalTransaction.class);
        return transactions != null ? transactions : new ArrayList<>();
    }

    @Override
    public <S extends RentalTransaction> S save(S entity) {
        return repository.save(entity);
    }
}
