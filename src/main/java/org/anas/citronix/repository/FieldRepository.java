package org.anas.citronix.repository;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {

    List<Field> findAllByFarm(Farm farm);

    Page<Field> findByRemoved(boolean removed, Pageable pageable);

}
