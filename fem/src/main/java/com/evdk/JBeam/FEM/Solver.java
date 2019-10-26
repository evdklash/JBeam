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

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class Solver {

    private SystemMatrix GSM;
    private DisplacementVector deltaVec;
    private SystemLoadVector loadVec;
    private StatusVector statusVec;
    private double[] reaction;
    private final int nEq;
    private int row, col;
    private final FEModel model;

    public Solver(FEModel model) {
        this.model = model;
        nEq = (int) model.getNumDofs();
        setupSystemOfEquations();

    }

    private void setupSystemOfEquations() {
        GSM = new SystemMatrix(nEq, model.getElementList());
        deltaVec = new DisplacementVector(nEq, model.getNodeSupportList());
        loadVec = new SystemLoadVector(nEq, model.getNodeForceList());
        statusVec = new StatusVector(nEq, model.getNodeSupportList());
        reaction = new double[nEq];
    }

    public void solve() {
//        GSM.printSystemMatrix();
        decompose();
//        GSM.printSystemMatrix();
//        loadVec.printLoadVector();
//        statusVec.printStatus();
        substitute();
        solveDisplacement();
//        deltaVec.printDisplacementVector();
        solveReaction();
//        printReaction();
        calculateElementForces();
    }
    
    private void decompose() {
        int[] colProfile = new int[nEq];
        int[] profile = GSM.getProfile();
        boolean[] status = statusVec.getStatus();
        double wTemp[] = new double[nEq];
        int pivot, row, col, i, startIndex;
        double sum, diagonal;

        // Compute the column profile of the matrix
        for (col = 0; col < nEq; col++) {
            colProfile[col] = 0;
        }
        for (row = 0; row < nEq; row++) {
            if (colProfile[profile[row]] < row) {
                colProfile[profile[row]] = row;
            }
        }
        for (col = 1; col < nEq; col++) {
            if (colProfile[col] < colProfile[col - 1]) {
                colProfile[col] = colProfile[col - 1];
            }
        }

        // loop on the columns of A
        for (pivot = 0; pivot < nEq; pivot++) {
            if (status[pivot]) {
                continue;
            }

            // compute the temporary vector w[]
            for (row = profile[pivot]; row < pivot; row++) {
                if (status[row]) {
                    continue;
                }
                wTemp[row] = GSM.getValue(row, row) * GSM.getValue(pivot, row);
            }

            // compute the diagonal element --> matrix[pivot][pivot]
            // d[p] = A[p][p] - Sum {A[p][i] * w[i]}
            diagonal = GSM.getValue(pivot, pivot);
            for (i = profile[pivot]; i < pivot; i++) {
                if (status[i]) {
                    continue;
                }
                diagonal -= GSM.getValue(pivot, i) * wTemp[i];
            }
            GSM.setValue(pivot, pivot, diagonal);

            // compute off-diagonal elements --> matrix[row][pivot]
            // A[r][p] = ( A[r][p] - Sum {A[r][i] * w[i]} ) / d[p]
            for (row = pivot + 1; row <= colProfile[pivot]; row++) {
                if (profile[row] > pivot) {
                    continue;
                }
                if (status[row]) {
                    continue;
                }
                startIndex = Math.max(profile[pivot], profile[row]);
                sum = GSM.getValue(row, pivot);
                for (i = startIndex; i < pivot; i++) {
                    if (status[i]) {
                        continue;
                    }
                    sum -= GSM.getValue(row, i) * wTemp[i];
                }
                sum /= diagonal;
                GSM.setValue(row, pivot, sum);
            }
        }
    }

    // substitute the prescribed displacements în the lower triangle
    public void substitute() {

        for (row = 0; row < nEq; row++) {
            if (statusVec.getStatus()[row]) {
                continue;
            }
            deltaVec.getDisplacementVector()[row] = loadVec.getLoadVector()[row];
            for (col = GSM.getProfile()[row]; col < row; col++) {
                if (!statusVec.getStatus()[col]) {
                    continue;
                }
                deltaVec.getDisplacementVector()[row] -= GSM.getValue(row, col) * deltaVec.getDisplacementVector()[col];
            }
        }

        // substitute the prescribed displacements în the upper triangle
        for (col = 0; col < nEq; col++) {
            if (!statusVec.getStatus()[col]) {
                continue;
            }
            for (row = GSM.getProfile()[col]; row < col; row++) {
                if (statusVec.getStatus()[row]) {
                    continue;
                }
                deltaVec.getDisplacementVector()[row] -= GSM.getValue(col, row) * deltaVec.getDisplacementVector()[col];
            }
        }
    }

    public void solveDisplacement() {

        // forward sweep for free displacements
        for (row = 0; row < nEq; row++) {
            if (statusVec.getStatus()[row]) {
                continue;
            }
            for (col = GSM.getProfile()[row]; col < row; col++) {
                if (statusVec.getStatus()[col]) {
                    continue;
                }
                deltaVec.getDisplacementVector()[row] -= GSM.getValue(row, col) * deltaVec.getDisplacementVector()[col];
            }
        }

        // divide all free rows by diagonal coefficient
        for (row = 0; row < nEq; row++) {
            if (statusVec.getStatus()[row]) {
                continue;
            }
            deltaVec.getDisplacementVector()[row] /= GSM.getValue(row, row);
        }

        // backward sweep for free displacements
        for (col = nEq - 1; col >= 0; col--) {
            if (statusVec.getStatus()[col]) {
                continue;
            }
            for (row = GSM.getProfile()[col]; row < col; row++) {
                if (statusVec.getStatus()[row]) {
                    continue;
                };
                deltaVec.getDisplacementVector()[row] -= GSM.getValue(col, row) * deltaVec.getDisplacementVector()[col];
            }
        }
    }
    
    public double[] getDisplacement(int[] doflist) {
        double[] delta = new double[doflist.length];
        for (int i = 0; i < doflist.length; i++) {
            delta[i] = deltaVec.getDisplacementVector()[doflist[i]];
        }
        return delta;
    }

    public void solveReaction() {
        // substitute the displacements in the rows of the lower triangle
        for (row = 0; row < nEq; row++) {
            reaction[row] = 0.0;
            if (!statusVec.getStatus()[row]) {
                continue;
            }
            reaction[row] = -loadVec.getLoadVector()[row];
            for (col = GSM.getProfile()[row]; col <= row; col++) {
                reaction[row] += GSM.getValue(row, col) * deltaVec.getDisplacementVector()[col];
            }
        }

        // substitute the displacements in the columns of upper the triangle
        for (col = 0; col < nEq; col++) {
            for (row = GSM.getProfile()[col]; row < col; row++) {
                if (!statusVec.getStatus()[row]) {
                    continue;
                }
                reaction[row] += GSM.getValue(col, row) * deltaVec.getDisplacementVector()[col];
            }
        }
    }

    public void printReaction() {
        for (int i = 0; i < reaction.length; i++) {
            System.out.print(String.format("%9.2e\n", reaction[i]));
        }
        System.out.println("");
    }
    // For plotting purposes

    public double[] getVerticalDisplacement() {
        double[] deltaY = new double[deltaVec.getDisplacementVector().length / 2];
        int count = 0;
        for (int i = 0; i < deltaVec.getDisplacementVector().length; i = i + 2) {
            deltaY[count] = deltaVec.getDisplacementVector()[i];
            count++;
        }
        return deltaY;
    }

    //Todo: Implement method to extract element forces
    public void calculateElementForces() {
        Session.model.getElementList().forEach(element -> {
             double[] delta = getDisplacement(element.getSystemIndex());
             element.calculateElementForces(new Array2DRowRealMatrix(delta));
        });
    }
}
