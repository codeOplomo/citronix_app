package org.anas.citronix.repository;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreeRepository extends JpaRepository<Tree, UUID> {
    long countByField(Field field);

    List<Tree> findAllByField(Field field);
}
