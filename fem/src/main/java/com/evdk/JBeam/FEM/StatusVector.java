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

public class StatusVector {

    private int dim;
    private boolean[] status;
    private Iterator supportIterator;

    public StatusVector(int dim, ArrayList<NodeSupport> supports) {
        this.dim = dim;
        status = new boolean[dim];
        supportIterator = supports.iterator();
        clearStatus();
        while (supportIterator.hasNext()) {
            NodeSupport s = (NodeSupport) supportIterator.next();
            setStatus(s.getSystemIndex(), true);
        }
    }

    private void clearStatus() {
        for (int k = 0; k < dim; k++) {
            status[k] = false;
        }
    }

    private void setStatus(int row, boolean value) {
        status[row] = value;
    }

    public boolean[] getStatus() {
        return status;
    }

    public void printStatus() {
        for (int i = 0; i < status.length; i++) {
            System.out.println(status[i]);
        }
        System.out.println();
    }
}