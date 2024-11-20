package org.anas.citronix.web.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class FarmVM {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String location;

    @NotNull
    @Positive
    private double area;

    private List<FieldVM> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public List<FieldVM> getFields() {
        return fields;
    }

    public void setFields(List<FieldVM> fields) {
        this.fields = fields;
    }
}
