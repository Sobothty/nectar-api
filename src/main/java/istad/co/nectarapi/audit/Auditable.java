package istad.co.nectarapi.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;


    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
        if (createdBy == null) {
            createdBy = "SYSTEM";
        }
        if (updatedBy == null) {
            updatedBy = "SYSTEM";
        }
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = Instant.now();
        if (updatedBy == null) {
            updatedBy = "SYSTEM";
        }
    }
}