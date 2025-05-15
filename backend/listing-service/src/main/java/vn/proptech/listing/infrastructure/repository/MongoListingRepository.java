package vn.proptech.listing.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.proptech.listing.domain.model.Listing;
import vn.proptech.listing.domain.model.ListingType;
import vn.proptech.listing.domain.repository.ListingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoListingRepository implements ListingRepository {
    
    private final MongoTemplate mongoTemplate;
    private final SpringDataMongoListingRepository repository;
    
    @Override
    public List<Listing> findAll() {
        Query query = new Query(Criteria.where("isActive").is(true));
        List<Listing> listings = mongoTemplate.find(query, Listing.class);
        return listings;
    }
    
    @Override
    public Optional<Listing> findById(String id) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("id").is(id));
        Listing listing = mongoTemplate.findOne(query, Listing.class);
        return Optional.ofNullable(listing);
    }
    
    @Override
    public <S extends Listing> S save(S entity) {
        return repository.save(entity);
    }
       
    @Override
    public long count() {
        return repository.count();
    }
    
    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
    
    @Override
    public List<Listing> search(
            ListingType listingType,
            Double minPrice,
            Double maxPrice,
            Integer minBedrooms,
            Integer maxBedrooms,
            Double minArea,
            Double maxArea,
            Integer limit,
            Integer offset,
            List<String> propertyIds) {
        
        Query query = buildSearchQuery(
                listingType,
                minPrice,
                maxPrice,
                minBedrooms,
                maxBedrooms,
                minArea,
                maxArea,
                propertyIds
        );
        
        // Default to 10 items per page if not specified
        int pageSize = limit != null ? limit : 10;
        int pageNumber = offset != null ? offset / pageSize : 0;
        
        query.with(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        
        return mongoTemplate.find(query, Listing.class);
    }
    
    @Override
    public long countSearchResults(
            ListingType listingType,
            Double minPrice,
            Double maxPrice,
            Integer minBedrooms,
            Integer maxBedrooms,
            Double minArea,
            Double maxArea,
            List<String> propertyIds) {
        
        Query query = buildSearchQuery(
                listingType,
                minPrice,
                maxPrice,
                minBedrooms,
                maxBedrooms,
                minArea,
                maxArea,
                propertyIds
        );
        
        return mongoTemplate.count(query, Listing.class);
    }
    
    
    @Override
    public List<Listing> findByAgentId(String agentId, Integer limit, Integer offset) {
        Query query = new Query(Criteria.where("agentId").is(agentId).and("isActive").is(true));
        int pageSize = limit != null ? limit : 10;
        int pageNumber = offset != null ? offset / pageSize : 0;
        query.with(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        return mongoTemplate.find(query, Listing.class);
        
    }

    @Override
    public List<Listing> findByLocation(double minLat, double maxLat, double minLon, double maxLon) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address.latitude").gte(minLat).lte(maxLat));
        query.addCriteria(Criteria.where("address.longitude").gte(minLon).lte(maxLon));
        query.addCriteria(Criteria.where("isActive").is(true));

        return mongoTemplate.find(query, Listing.class);
    }

    @Override
    public List<Listing> findByPropertyIds(List<String> propertyIds) {
        if (propertyIds == null || propertyIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        Query query = new Query();
        query.addCriteria(Criteria.where("propertyId").in(propertyIds));
        query.addCriteria(Criteria.where("isActive").is(true));
        
        return mongoTemplate.find(query, Listing.class);
    }

    private Query buildSearchQuery(
            ListingType listingType,
            Double minPrice,
            Double maxPrice,
            Integer minBedrooms,
            Integer maxBedrooms,
            Double minArea,
            Double maxArea,
            List<String> propertyIds) {
        
        List<Criteria> criteriaList = new ArrayList<>();
        
        // Only show active listings
        criteriaList.add(Criteria.where("isActive").is(true));
        
        // Thêm các tiêu chí tìm kiếm khác nếu chúng không phải là null
        if (listingType != null) {
            criteriaList.add(Criteria.where("listingType").is(listingType));
        }

        // Xử lý giá (price)
        handleMinMaxRange(criteriaList, "price", minPrice, maxPrice);
        
        // Xử lý số phòng ngủ (bedrooms)
        handleMinMaxRange(criteriaList, "bedrooms", minBedrooms, maxBedrooms);
        
        // Xử lý diện tích (area)
        handleMinMaxRange(criteriaList, "area", minArea, maxArea);

        // Xử lý danh sách propertyIds
        if (propertyIds != null && !propertyIds.isEmpty()) {
            criteriaList.add(Criteria.where("propertyId").in(propertyIds));
        }
        
        // Tạo query với các tiêu chí đã được thêm vào
        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        
        return query;
    }
    
    /**
     * Helper method to handle min/max range queries
     * @param criteriaList List to add the criteria to
     * @param fieldName Field name to query
     * @param minValue Minimum value (can be null)
     * @param maxValue Maximum value (can be null)
     */
    private <T extends Comparable<T>> void handleMinMaxRange(List<Criteria> criteriaList, String fieldName, T minValue, T maxValue) {
        if (minValue != null && maxValue != null) {
            criteriaList.add(Criteria.where(fieldName).gte(minValue).lte(maxValue));
        } else if (minValue != null) {
            criteriaList.add(Criteria.where(fieldName).gte(minValue));
        } else if (maxValue != null) {
            criteriaList.add(Criteria.where(fieldName).lte(maxValue));
        }
    }
}