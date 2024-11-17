package org.anas.citronix.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.anas.citronix.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface FarmRepository extends JpaRepository<Farm, UUID> {

    default List<Farm> searchFarms(String name, String location, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Farm> cq = cb.createQuery(Farm.class);
        Root<Farm> farmRoot = cq.from(Farm.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(farmRoot.get("name"), "%" + name + "%"));
        }
        if (location != null && !location.isEmpty()) {
            predicates.add(cb.like(farmRoot.get("location"), "%" + location + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Farm> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

