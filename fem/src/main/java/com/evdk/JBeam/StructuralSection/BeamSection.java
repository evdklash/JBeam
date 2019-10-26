/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.StructuralSection;

/**
 *
 * @author Etienne
 */
public interface BeamSection {
    
    public double getArea();
    public double getIxx();
    public double getIyy();
    public void printIxx();
    
}
