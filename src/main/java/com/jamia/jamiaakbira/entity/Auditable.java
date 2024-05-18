package com.jamia.jamiaakbira.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jamia.jamiaakbira.exception.EntityException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;


import static com.jamia.jamiaakbira.domain.RequestContext.getUserId;
import static java.time.LocalDateTime.now;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    @NotNull
    @Column(name = "created_by")
    private Long createdBy;
    @NotNull
    @Column(name = "updated_by")
    private Long updatedBy;
    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    private boolean deleted;


    @PrePersist
    public void prePersist() throws EntityException {
        var userId = getUserId();
        if (userId == null) {
            throw new EntityException("cannot persist entity without user ID in the Request Context of this thread.");
        }
        setCreatedAt(now());
        setCreatedBy(userId);
        setUpdatedAt(now());
        setUpdatedBy(userId);
    }

    @PreUpdate
    public void beforeUpdate() throws EntityException {
        var userId =getUserId();
        if (userId == null) {
            throw new EntityException("cannot update entity without user ID in the Request Context of this thread.");
        }
        setUpdatedAt(now());
        setUpdatedBy(userId);
    }


}
