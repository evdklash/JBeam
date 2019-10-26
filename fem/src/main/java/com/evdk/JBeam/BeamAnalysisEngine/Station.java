/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.BeamAnalysisEngine;

import com.evdk.JBeam.FEM.*;

/**
 *
 * @author Etienne
 */
public class Station extends FeNode {
    
    private final Enum stationType;
    
    //Todo: Implement aotomatic node numbering so that the next avaliable node number is assigned to a new station or evenetually a new element.
    public Station(int nodeNumber, double x, double y, double z, Enum type) {
        super(nodeNumber, x, y, z);
        this.stationType = type;
    }

    
    public Enum getType(){
        return stationType;
    }
    
    
}
