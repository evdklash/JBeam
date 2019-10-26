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
public class FeNode implements Coordinate3D {

    private int nodeNumber;
    private final double x, y, z;
    final private int[] systemIndices;
    public ArrayList<Double[]> displacementHistory;
        
    /**
     *
     * @param nodeNumber The nodes number. Nodes must have a unique number
     * @param x The node's coordinate along the cartesian x-axis
     * @param y The node's coordinate along the cartesian y-axis
     * @param z The node's coordinate along the cartesian z-axis
     */
    public FeNode(int nodeNumber, double x, double y, double z) {
        this.nodeNumber = nodeNumber;
        this.x = x;
        this.y = y;
        this.z = z;
        systemIndices = new int[3];
        initialiseHistory();
        
    }

    /**
     * Construct a {@link #Node(int, double, double, double)} object using a
     * default value of zero for the z-coordinate
     *
     * @param nodeNumber
     * @param x
     * @param y
     * @see #Node(int, double, double, double)
     */
    public FeNode(int nodeNumber, double x, double y) {
        this(nodeNumber, x, y, 0);
    }

    /**
     * Construct a {@link #Node(int, double, double, double)} object using a
     * default value of zero for the y and z-coordinate
     *
     * @param nodeNumber
     * @param x
     * @see #Node(int, double, double, double)
     */
    public FeNode(int nodeNumber, double x) {
        this(nodeNumber, x, 0, 0);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public int[] getSystemIndices() {
        systemIndices[0] = 2 * nodeNumber - 2;
        systemIndices[1] = 2 * nodeNumber - 1;
        return systemIndices;
    }

    private void initialiseHistory() {
        displacementHistory = new ArrayList<>();
        Double array[] = new Double[3];
        for (int i = 0; i < 3; i++) {
            array[i] = 0.0;
        }
        displacementHistory.add(array);
    }
    

}
