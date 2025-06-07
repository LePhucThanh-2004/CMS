package vn.edu.ntu.cms.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long searchCount = 1L;

    @Column(nullable = false)
    private LocalDateTime lastSearchedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastSearchedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastSearchedAt = LocalDateTime.now();
    }

    public void incrementSearchCount() {
        this.searchCount++;
    }
} 