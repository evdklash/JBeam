/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evdk.JBeam.BeamAnalysisEngine;

import com.evdk.JBeam.FEM.*;
import com.evdk.JBeam.StructuralSection.*;

/**
 *
 * @author Etienne
 */
public class ConcreteBeam extends Beam {
    
    private final Section section;
    private final Station start;
    private final Station end;

    public ConcreteBeam(Section section, Station start, Station end) {
        super(section, start, end);
        this.section = section;
        this.start = start;
        this.end= end;
    }

    @Override
    public BeamSection getCrossSection() {
        return (BeamSection) section;
    }

    @Override
    public FeNode[] getNodes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Station[] getStations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
