package vn.proptech.listing.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.proptech.listing.domain.model.Property;
import vn.proptech.listing.domain.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MongoPropertyRepository implements PropertyRepository {

    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoPropertyRepository repository;

    @Override
    public List<Property> findAll(Integer limit, Integer offset) {
        // Lấy tất cả các property đang active
        Query query = new Query(Criteria.where("isActive").is(true));
        query.with(Pageable.ofSize(limit).withPage(offset));
        return mongoTemplate.find(query, Property.class);

    }

    @Override
    public Optional<Property> findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Property.class));
    }

    @Override
    public <S extends Property> S save(S entity) {
        return repository.save(entity);
    }
    
    @Override
    public List<Property> findByLocation(double minLat, double maxLat, double minLon, double maxLon) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address.latitude").gte(minLat).lte(maxLat));
        query.addCriteria(Criteria.where("address.longitude").gte(minLon).lte(maxLon));
        query.addCriteria(Criteria.where("isActive").is(true));
        
        return mongoTemplate.find(query, Property.class);
    }
    
    @Override
    public List<String> findPropertyIdsByLocation(double minLat, double maxLat, double minLon, double maxLon) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address.latitude").gte(minLat).lte(maxLat));
        query.addCriteria(Criteria.where("address.longitude").gte(minLon).lte(maxLon));
        query.addCriteria(Criteria.where("isActive").is(true));
        
        // Chỉ lấy trường id
        query.fields().include("id");
        
        List<Property> properties = mongoTemplate.find(query, Property.class);
        return properties.stream()
                .map(Property::getId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Property> findByAddressKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Tìm kiếm các property có địa chỉ chứa từ khóa (không phân biệt chữ hoa/thường)
        Query query = new Query();
        
        // Tạo OR criteria để tìm từ khóa trong các thành phần địa chỉ
        Criteria addressCriteria = new Criteria().orOperator(
                Criteria.where("address.street").regex(keyword, "i"),
                Criteria.where("address.district").regex(keyword, "i"),
                Criteria.where("address.city").regex(keyword, "i"),
                Criteria.where("address.province").regex(keyword, "i"),
                Criteria.where("address.country").regex(keyword, "i"),
                Criteria.where("address.postalCode").regex(keyword, "i")
        );
        
        query.addCriteria(addressCriteria);
        query.addCriteria(Criteria.where("isActive").is(true));
        
        return mongoTemplate.find(query, Property.class);
    }
}