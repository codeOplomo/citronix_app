package org.anas.citronix.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Table(name = "trees")
public class Tree {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate plantingDate;

    @ManyToOne(optional = false)
    private Field field;

    // Helper Methods
    public int calculateAge() {
        return Period.between(plantingDate, LocalDate.now()).getYears();
    }

    public double calculateProductivity() {
        int age = calculateAge();
        if (age < 3) return 2.5;
        else if (age <= 10) return 12.0;
        else if (age <= 20) return 20.0;
        else return 0.0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
