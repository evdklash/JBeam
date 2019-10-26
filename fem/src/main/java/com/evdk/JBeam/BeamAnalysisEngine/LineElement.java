/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.BeamAnalysisEngine;

import com.evdk.JBeam.StructuralSection.*;
import com.evdk.JBeam.FEM.*;

/**
 *
 * @author Etienne
 */
public interface LineElement {
    
    public BeamSection getCrossSection();
    public FeNode[] getNodes();
    public Station[] getStations();
        
    
}
