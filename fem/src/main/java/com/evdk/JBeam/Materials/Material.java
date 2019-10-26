/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.Materials;

import java.util.Map;

/**
 *
 * @author Etienne
 */
public class Material {
    
    private final Enum<MaterialType> type;
    private final Map<String, Double> properties;
    
    public Material(Enum<MaterialType> type, Map<String, Double> properties) {
        this.type = type;
        this.properties = properties;
    }
    
    public Enum<MaterialType> getType() {
        return type;
    }
    
    public double getEmodulus() {
        return properties.get("E");
    }
    
    public double getDensity() {
        return properties.get("rho");
    }

    public void printProperties(Map<String, Double> properties) {
        properties.forEach((key, value) -> {
            System.out.println("Key : " + key + ", Value : " + value);
        });
    }

}
