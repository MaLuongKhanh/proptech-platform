package vn.proptech.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "permissions")
public class Permission {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String description;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active = true;
}