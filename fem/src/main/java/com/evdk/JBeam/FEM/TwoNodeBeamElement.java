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

import com.evdk.JBeam.StructuralSection.*;
import static com.evdk.JBeam.FEM.Dimension.*;
import com.evdk.JBeam.Materials.*;
import java.util.ArrayList;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class TwoNodeBeamElement implements FiniteElement {

    private final String id;
    private final FeNode n1, n2;
    private final Material m;
    private final Section s;
    private final double E, I;
    private final ArrayList<FeNode> nodeList;
    private final int elDof;
    private final Enum problemClass;
    private RealMatrix mat;
    RealMatrix elForces;
    private Input in;

    public TwoNodeBeamElement(FeNode node1, FeNode node2, Material mat, Section section, Input in) {
        this.nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node2);
        id = "E: " + Integer.toString(node1.getNodeNumber()) + "-" + Integer.toString(node2.getNodeNumber());
        n1 = node1;
        n2 = node2;
        m = mat;
        s = section;
        E = m.getEmodulus();
        I = s.getMomentOfInertia();
        problemClass = Input.space;
        elDof = 2;
        this.in = in;
        applyOwnWeight();
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public ArrayList<FeNode> getNodeList() {
        return nodeList;
    }

    public double getLength() {
        double length = 0.0;
        if (problemClass == TwoD) {
            double dx = n1.getX() - n2.getX();
            double dy = n1.getY() - n2.getY();
            length = Math.sqrt(dx * dx + dy * dy);
        } else if (problemClass == ThreeD) {
            double dx = n1.getX() - n2.getX();
            double dy = n1.getY() - n2.getY();
            double dz = n1.getZ() - n2.getZ();
            length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        return length;
    }

    @Override
    public final int[] getSystemIndex() {
        int[] indices = null;
        if (problemClass == TwoD) {
            indices = new int[4];
            indices[0] = elDof * n1.getNodeNumber() - 2;
            indices[1] = elDof * n1.getNodeNumber() - 1;
            indices[2] = elDof * n2.getNodeNumber() - 2;
            indices[3] = elDof * n2.getNodeNumber() - 1;
        }
//        Utility.printVector(indices, 10);
        return indices;
    }

    @Override
    public RealMatrix getElementMatrix() {
        double[][] ESM = new double[4][4];
        for (double[] row : ESM) {
            for (int j = 0; j < ESM[0].length; j++) {
                row[j] = 0.0;
            }
        }

        double phi = 0.0; // Todo: Implement Timoshenko beam effect
        double L = this.getLength();
        double a = 12.0;
        double b = 6.0 * L;
        double c = -a;
        double d = (4.0 + phi) * Math.pow(L, 2);
        double e = -b;
        double f = (2 - phi) * Math.pow(L, 2);
        double C = (E * I) / (Math.pow(L, 3)) * (1 + phi);

        ESM[0][0] = a;
        ESM[0][1] = b;
        ESM[0][2] = c;
        ESM[0][3] = b;

        ESM[1][0] = b;
        ESM[1][1] = d;
        ESM[1][2] = e;
        ESM[1][3] = f;

        ESM[2][0] = c;
        ESM[2][1] = e;
        ESM[2][2] = a;
        ESM[2][3] = e;

        ESM[3][0] = b;
        ESM[3][1] = f;
        ESM[3][2] = e;
        ESM[3][3] = d;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ESM[i][j] = ESM[i][j] * C;
            }
        }

        mat = new BlockRealMatrix(ESM);
        return mat;
    }

    @Override
    public int getElementNumDof() {
        return elDof;
    }

    @Override
    public RealMatrix calculateElementForces(RealMatrix displacementVector) {
        elForces = mat.multiply(displacementVector);
        return elForces;
    }

    public double[] getElementForces() {
        return elForces.getColumn(0);
    }

    public double[] getBeamMoments() {
        double[] moments = new double[2];
        moments[0] = elForces.getEntry(1, 0);
        moments[1] = elForces.getEntry(3, 0);
        return moments;
    }

    public double[] getBeamShearForces() {
        double[] shears = new double[2];
        shears[0] = elForces.getEntry(0, 0);
        shears[1] = elForces.getEntry(2, 0);
        return shears;
    }

    private void applyOwnWeight() {
        double ownWeightUdl = s.getArea() * m.getDensity() * 9.81;
        BeamLoad bl = new BeamLoad(in, this, ownWeightUdl);
    }

    private String printElementForces() {
        double[] endMoments = getBeamMoments();
        double[] endShears = getBeamShearForces();
        StringBuilder sb = new StringBuilder();
        sb.append("Element ").append(getID()).append(" has end forces:\n");
        for (int i = 0; i < endMoments.length; i++) {
            String s1 = String.format("V%d = %+.0f N\n", i + 1, endShears[i]);
            String s2 = String.format("M%d = %+.0f\u2009Nm\n", i + 1, endMoments[i]);
            String s = s1 + s2;
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return printElementForces();
    }

}
