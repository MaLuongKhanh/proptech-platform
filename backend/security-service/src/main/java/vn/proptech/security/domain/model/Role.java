package vn.proptech.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String description;
    private Set<String> permissions = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active = true;
}