package vn.proptech.listing.domain.repository;

import org.springframework.stereotype.Repository;
import vn.proptech.listing.domain.model.Property;

import java.util.Optional;
import java.util.List;

@Repository
public interface PropertyRepository {
    List<Property> findAll(Integer limit, Integer offset);

    Optional<Property> findById(String id);

    <S extends Property> S save(S entity);
    
    // Tìm các property dựa trên tọa độ vị trí
    List<Property> findByLocation(double minLat, double maxLat, double minLon, double maxLon);
    
    // Lấy id của các property trong một phạm vi vị trí
    List<String> findPropertyIdsByLocation(double minLat, double maxLat, double minLon, double maxLon);
    
    // Tìm kiếm property theo từ khóa địa chỉ
    List<Property> findByAddressKeyword(String keyword);
}
