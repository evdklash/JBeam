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

public class SystemMatrix {

    private final int n;
    private final int[] profile;
    private final double matrix[][];

    public SystemMatrix(int numberOfRows, ArrayList<FiniteElement> elementList) {
        n = numberOfRows;
        profile = new int[n];
        for (int row = 0; row < n; row++) {
            profile[row] = row;
        }
        // Loop over elementlist to set profile  vector
        Iterator elementIterator = elementList.iterator();
        while (elementIterator.hasNext()) {
            TwoNodeBeamElement e = (TwoNodeBeamElement) elementIterator.next();
            int[] index = e.getSystemIndex();
            setProfile(index);
        }
        matrix = new double[n][];
        initRowsOfMatrix();
        // Loop over elements and add them to the profiled system matrix
        elementIterator = elementList.iterator();
        while (elementIterator.hasNext()) {
            TwoNodeBeamElement e = (TwoNodeBeamElement) elementIterator.next();
            int[] index = e.getSystemIndex();
            addSubmatrix(index, e.getElementMatrix().getData());
        }

    }

    public void setValue(int row, int col, double value) {
        matrix[row][col - profile[row]] = value;
    }

    public void addValue(int row, int col, double value) {
        matrix[row][col - profile[row]] += value;
    }

    public double getValue(int row, int col) {
        return matrix[row][col - profile[row]];
    }

    private int[] setProfile(int index[]) {
        for (int i = 0; i < index.length; i++) {
            int row = index[i];
            for (int j = 0; j < index.length; j++) {
                if (profile[row] > index[j]) {
                    profile[row] = index[j];
                }
            }
        }
        return profile;
    }

    private void initRowsOfMatrix() {
        for (int row = 0; row < n; row++) {
            int size = row - profile[row] + 1;
            matrix[row] = new double[size];
        }
    }

    private void addSubmatrix(int index[], double subMatrix[][]) {
        for (int i = 0; i < index.length; i++) {
            int row = index[i];
            for (int j = 0; j < index.length; j++) {
                int col = index[j];
                if (col > row) {
                    continue;
                }
                matrix[row][col - profile[row]] += subMatrix[i][j];
            }
        }
    }

    public int[] getProfile() {
        return profile;
    }

    public void printSystemMatrix() {
        System.out.println();
        int maxLength = 0;      
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                String str = String.format("%.2e", matrix[i][j]);
                String strWholePart = str.substring(0, str.indexOf('e'));
                if (strWholePart.length() > maxLength) {
                    maxLength = strWholePart.length();
//                    System.out.println("Max string length = " + maxLength);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                String str = String.format("%.2e", matrix[i][j]);
                int padding = Math.max(1, maxLength + 4 + 1 - str.length());
                for (int k = 0; k < padding; k++) {
                    System.out.print(' ');
                }
                System.out.print(str);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printProfileVector() {
        Utility.printVector(profile, 10);
    }
}
