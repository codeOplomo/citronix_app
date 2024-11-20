package org.anas.citronix.repository;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, UUID> {
    boolean existsByFieldAndSeason(Field field, Season season);

    List<Harvest> findBySeason(Season season);
}
