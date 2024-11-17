package org.anas.citronix.repository;

import org.anas.citronix.domain.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
}
