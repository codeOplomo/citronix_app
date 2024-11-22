package org.anas.citronix.repository;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreeRepository extends JpaRepository<Tree, UUID> {
    long countByField(Field field);

    List<Tree> findAllByField(Field field);

    @Query("SELECT t FROM Tree t WHERE t.removed = false")
    List<Tree> findAllActiveTrees();

    Page<Tree> findAllByRemoved(boolean removed, Pageable pageable);
}
