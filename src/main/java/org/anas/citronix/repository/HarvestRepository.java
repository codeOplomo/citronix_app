package org.anas.citronix.repository;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, UUID> {

    @Query("SELECT COUNT(h) > 0 FROM Harvest h WHERE h.field = :field AND h.season = :season AND YEAR(h.harvestDate) = :year")
    boolean existsByFieldAndSeasonAndYear(@Param("field") Field field, @Param("season") Season season, @Param("year") int year);

    List<Harvest> findBySeason(Season season);

    List<Harvest> findAllByField(Field field);
}
