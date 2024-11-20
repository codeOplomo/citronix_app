package org.anas.citronix.web.vm;

import jakarta.validation.constraints.Positive;

public class FieldVM {

    @Positive
    private double area;

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
