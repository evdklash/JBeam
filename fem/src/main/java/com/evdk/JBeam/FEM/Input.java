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

import java.util.ArrayList;
import java.util.HashMap;

import com.evdk.JBeam.Materials.*;
import com.evdk.JBeam.StructuralSection.*;

public class Input implements ModelInput {

    private final ArrayList<FeNode> nodeList;
    private final ArrayList<FiniteElement> elementList;
    private final ArrayList<NodeLoad> forceList;
    private final ArrayList<NodeSupport> supportList;
    HashMap<String, Double> materialProperties;
    private final Material steel;
    private final Section sec;
    public static Enum<Dimension> space;
    int numel;

    public Input() {

        numel = 51;

        //TODO: Define generic input method and user interface so that beam dimensions etc are defined by distances
        nodeList = new ArrayList<>();
        elementList = new ArrayList<>();
        forceList = new ArrayList<>();
        supportList = new ArrayList<>();
        materialProperties = new HashMap<>();

        space = Dimension.TwoD;
        materialProperties.put("fcu", 30.0); // Compressive strength in Pa
        materialProperties.put("E", 30e9); // Elastic modulus in Pa
        materialProperties.put("rho", 2400.0); // Density of material in kg/m3

        steel = new Material(MaterialType.CONCRETE, materialProperties);
        double width, height, area, Ixx;
        width = 0.1; // The width of the section in meter
        height = 0.3; // The height of the section in meter
        area = width * height;
        Ixx = width * Math.pow(height, 3) / 12.0;
        sec = new Section("s1", area, Ixx);

        createNodeList();
        int[] forceNodes = {(nodeList.size() / 2 + nodeList.size() % 2), (nodeList.size() * 3 / 4 + nodeList.size() % 4)};
        Key[] forceComponets = {Key.Y, Key.Y};
        Double[] forceValues = {-1000.006, -10000.0};
        int[] supportNodes = {1,
            nodeList.size() / 3 + nodeList.size() % 3,
            nodeList.size() * 2 / 3 + nodeList.size() % 3,
            nodeList.size()
        };

        for (int i = 0; i < forceNodes.length; i++) {
            forceList.add(new NodeLoad(forceNodes[i], forceComponets[i], forceValues[i]));
        }
        for (int i = 0; i < supportNodes.length; i++) {
            supportList.add(new NodeSupport(supportNodes[i], Key.Y, 0.0));
        }

    }

    private void createNodeList() {
        for (int i = 1; i <= numel; i++) {
            nodeList.add(new FeNode(i, (i - 1) * 0.1, 0.0));
        }
        for (int i = 0; i < nodeList.size() - 1; i++) {
            FeNode n1 = nodeList.get(i);
            FeNode n2 = nodeList.get(i + 1);
            elementList.add(new TwoNodeBeamElement(n1, n2, steel, sec, this));
        }
    }

    @Override
    public Enum<Dimension> getModelSpace() {
        return space;
    }

    @Override
    public ArrayList<FeNode> getNodeList() {
        return nodeList;
    }

    @Override
    public ArrayList<FiniteElement> getElementList() {
        return elementList;
    }

    @Override
    public ArrayList<NodeSupport> getSupportList() {
        return supportList;
    }

    @Override
    public ArrayList<NodeLoad> getNodeForceList() {
        return forceList;
    }

    public void addNodeForce(NodeLoad nl) {
        forceList.add(nl);
    }

}
