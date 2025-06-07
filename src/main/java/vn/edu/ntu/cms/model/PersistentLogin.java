package vn.edu.ntu.cms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {
    @Column(nullable = false)
    private String username;

    @Id
    private String series;

    @Column(nullable = false)
    private String token;

    @Column(name = "last_used", nullable = false)
    private LocalDateTime lastUsed;
} 