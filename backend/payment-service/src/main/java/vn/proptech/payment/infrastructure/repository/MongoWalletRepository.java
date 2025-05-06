package vn.proptech.payment.infrastructure.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.proptech.payment.domain.model.Wallet;
import vn.proptech.payment.domain.repository.WalletRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoWalletRepository implements WalletRepository {
    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoWalletRepository repository;

    @Override
    public Wallet findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        Wallet wallet = mongoTemplate.findOne(query, Wallet.class);
        return wallet;
    }

    @Override
    public Wallet findByUserId(String userId) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        Wallet wallet = mongoTemplate.findOne(query, Wallet.class);
        return wallet;
    }

    @Override
    public <S extends Wallet> S save(S entity) {
        return repository.save(entity);
    }
}
