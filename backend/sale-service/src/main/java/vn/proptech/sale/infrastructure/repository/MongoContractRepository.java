package vn.proptech.sale.infrastructure.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.proptech.sale.domain.model.Contract;
import vn.proptech.sale.domain.repository.ContractRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoContractRepository implements ContractRepository {
    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoContractRepository repository;

    @Override
    public Contract findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        Contract contract = mongoTemplate.findOne(query, Contract.class);
        return contract;
    }

    @Override
    public List<Contract> findAll(
        Integer limit,
        Integer offset,
        String sort,
        String direction,
        String transactionId,
        String uploadedBy,
        Instant startDate,
        Instant endDate
    ) {
        Query query = new Query(Criteria.where("isActive").is(true));
        if (transactionId != null) {
            query.addCriteria(Criteria.where("transaction_id").is(transactionId));
        }
        if (uploadedBy != null) {
            query.addCriteria(Criteria.where("uploaded_by").is(uploadedBy));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("uploaded_at").gte(startDate).lte(endDate));
        }
        

        int pageSize = limit != null ? limit : 10;
        int pageOffset = offset != null ? offset : 0;
        if (sort != null && direction != null) {
            Sort sortDirection = Sort.by(Sort.Direction.fromString(direction), sort);
            query.with(PageRequest.of(pageOffset, pageSize, sortDirection));
        } else {
            query.with(PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "uploadedAt")));
        }
        List<Contract> contracts = mongoTemplate.find(query, Contract.class);
        return contracts != null ? contracts : new ArrayList<>();
    }

    @Override
    public <S extends Contract> S save(S entity) {
        return repository.save(entity);
    }
}
