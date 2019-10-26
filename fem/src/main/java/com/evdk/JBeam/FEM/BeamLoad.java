/* 
 * Copyright 2017 Etienne van der Klashorst.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evdk.JBeam.FEM;

/**
 *
 * @author Etienne
 */
public class BeamLoad {
    
    private final double w;
    private final TwoNodeBeamElement beam;
    private final double[] forceVector;
    private final Input in;
    
    public BeamLoad(Input in, TwoNodeBeamElement el, double udl) {
        this.in = in;
        this.beam = el;
        this.w = udl;
        forceVector = getEquivalentNodalLoads();
        addNodeLoads();
    }
    
    private double[] getEquivalentNodalLoads() {
        double[] f = new double[4];
        f[0] = w*beam.getLength() / 2.0;
        f[1] = -w*Math.pow(beam.getLength(),2)/12.0;
        f[2] = w*beam.getLength() / 2.0;
        f[3] = w*Math.pow(beam.getLength(),2)/12.0;
        return f;
    }
    
    private void addNodeLoads() {
        FeNode n1 = beam.getNodeList().get(0);
        FeNode n2 = beam.getNodeList().get(1);
        double f1 = forceVector[0];
        double f2 = forceVector[1];
        double f3 = forceVector[2];
        double f4 = forceVector[3];
        NodeLoad nl1 = new NodeLoad(n1.getNodeNumber(), Key.Y, -f1);
        NodeLoad nl2 = new NodeLoad(n1.getNodeNumber(), Key.z, -f2);
        NodeLoad nl3 = new NodeLoad(n2.getNodeNumber(), Key.Y, -f3);
        NodeLoad nl4 = new NodeLoad(n2.getNodeNumber(), Key.z, -f4);
        in.addNodeForce(nl1);
        in.addNodeForce(nl2);
        in.addNodeForce(nl3);
        in.addNodeForce(nl4);
    }
    
}
