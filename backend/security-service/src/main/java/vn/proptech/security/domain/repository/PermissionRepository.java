package vn.proptech.security.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.proptech.security.domain.model.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByName(String name);
    List<Permission> findByCategory(String category);
    boolean existsByName(String name);
}