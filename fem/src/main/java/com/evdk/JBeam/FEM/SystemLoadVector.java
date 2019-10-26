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
import java.util.Iterator;

public class SystemLoadVector {

    private ArrayList<NodeLoad> loads;
    private double[] loadVector;

    public SystemLoadVector(int dim, ArrayList<NodeLoad> forces) {
        loads = forces;
        Iterator loadIterator = loads.iterator();
        loadVector = new double[dim];
        while (loadIterator.hasNext()) {
            NodeLoad f = (NodeLoad) loadIterator.next();
            loadVector[f.getSystemIndex()] = loadVector[f.getSystemIndex()] + f.force;
        }
    }

    public double[] getLoadVector() {
        return loadVector;
    }

    public void printLoadVector() {
        for (int i = 0; i < loadVector.length; i++) {
            System.out.print(String.format("%9.2e\n", loadVector[i]));
        }
        System.out.println("");
    }
}
