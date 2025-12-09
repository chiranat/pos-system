package com.chiranat.pos.repository;

import com.chiranat.pos.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, UUID> {
    Optional<TableEntity> findByTableNumber(String tableNumber);
    List<TableEntity> findByStatus(TableEntity.TableStatus status);
}
