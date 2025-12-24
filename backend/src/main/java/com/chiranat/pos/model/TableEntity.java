package com.chiranat.pos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tables")
@SQLDelete(sql = "UPDATE tables SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class TableEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "table_number", nullable = false, unique = true, length = 50)
    private String tableNumber;

    @Column(name = "capacity", nullable = false)
    @Builder.Default
    private Integer capacity = 4;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private TableStatus status = TableStatus.AVAILABLE;

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum TableStatus {
        AVAILABLE, OCCUPIED
    }
}
