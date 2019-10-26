/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.StructuralSection;

import java.util.Map;

/**
 *
 * @author Etienne
 */
public class RectangularBeamSection implements BeamSection {

    private final Enum type;
    private final Map dims;
    private final double Area, Ixx, Iyy;

    public RectangularBeamSection(Enum type, Map<String, Double> dims) {

        this.type = type;
        this.dims = dims;

        double b = dims.get("width");
        double h = dims.get("depth");
        Area = b*h;
        Ixx = (1.0 / 12.0) * b * Math.pow(h, 3);
        Iyy = (1.0 / 12.0) * h * Math.pow(b, 3);
    }

    @Override
    public double getArea() {
        return Area;
    }
    
    @Override
    public double getIxx() {
        return Ixx;
    }
    
    @Override
    public double getIyy() {
        return Ixx;
    }

    public void printProperties(Map<String, Double> properties) {
        properties.forEach((key, value) -> {
            System.out.println("Key : " + key + ", Value : " + value);
        });
    }

    @Override
    public void printIxx() {
        System.out.format("Moment of inertia about X-X axis,"
                + " Ixx = %.2e mm\u2074\n", this.getIxx());
    }

}
