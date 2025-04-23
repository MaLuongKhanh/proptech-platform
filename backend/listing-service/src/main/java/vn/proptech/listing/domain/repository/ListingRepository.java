package vn.proptech.listing.domain.repository;

import org.springframework.stereotype.Repository;
import vn.proptech.listing.domain.model.Listing;
import vn.proptech.listing.domain.model.ListingType;
import vn.proptech.listing.domain.model.PropertyType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository {

    // Basic CRUD operations
    List<Listing> findAll();
    Optional<Listing> findById(String id);
    <S extends Listing> S save(S entity);
    long count();
    boolean existsById(String id);

    // Custom search operations
    List<Listing> search(
            ListingType listingType,
            PropertyType propertyType,
            String city,
            String district,
            Double minPrice,
            Double maxPrice,
            Integer minBedrooms,
            Integer maxBedrooms,
            Double minArea,
            Double maxArea,
            Integer limit,
            Integer offset);

    long countSearchResults(
            ListingType listingType,
            PropertyType propertyType,
            String city,
            String district,
            Double minPrice,
            Double maxPrice,
            Integer minBedrooms,
            Integer maxBedrooms,
            Double minArea,
            Double maxArea);

    List<Listing> findByAgentId(String agentId, Integer limit, Integer offset);

    // Geospatial operations
    List<Listing> findByLocation(double minLat, double maxLat, double minLon, double maxLon);
    
    // Tìm listings theo danh sách propertyIds
    List<Listing> findByPropertyIds(List<String> propertyIds);
}