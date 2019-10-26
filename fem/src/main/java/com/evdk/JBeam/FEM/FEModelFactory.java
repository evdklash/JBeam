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

/**
 *
 * @author Etienne
 */
public class FEModelFactory {

    private long nDof;
    public final FEModel m;

    public FEModelFactory() {
        
        m = new FEModel() {
            
            Input input = new Input();
            
            @Override
            public Enum<Dimension> getSpaceDimension() {
                return input.getModelSpace();
            }

            @Override
            public long getNumDofs() {
                ArrayList<FeNode> nodeList = input.getNodeList();
                nDof = nodeList.size()*2;
                return nDof;
            }

            @Override
            public FeNode getNode(int nodeNumber) {
                return input.getNodeList().get(nodeNumber);
            }

            @Override
            public FiniteElement getElement(int elNumber) {
                return input.getElementList().get(elNumber);
            }

            @Override
            public ArrayList<FiniteElement> getElementList() {
                return input.getElementList();
            }

            @Override
            public ArrayList<NodeSupport> getNodeSupportList() {
                return input.getSupportList();
            }

            @Override
            public ArrayList<NodeLoad> getNodeForceList() {
                return input.getNodeForceList();
            }

            @Override
            public ArrayList<FeNode> getNodeList() {
                return input.getNodeList();
            }
        };
        
        this.nDof = m.getNumDofs();

    }

    public FEModel getFEModel() {
        return m;
    }
}
