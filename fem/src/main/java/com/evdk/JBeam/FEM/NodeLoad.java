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

public class NodeLoad {

	public String name;
	private final int nodeNumber;
	public Key component;
	public double force;

	public NodeLoad(int nodeNumber, Key component, double force) {
		this.name = Integer.toString(nodeNumber) + component.name();
		this.nodeNumber = nodeNumber;
		this.component = component;
		this.force = force;
	}

	public int getSystemIndex() {
		int[] indices = getNode().getSystemIndices();
		switch (component) {
		case Y:
			return indices[0];
		case z:
			return indices[1];
		case X:
			break;
		case Z:
			break;
		case x:
			break;
		case y:
			break;
		default:
			break;
		}
		return -1;
	}

	public FeNode getNode() {
		return Session.model.getNode(nodeNumber - 1);
	}

	public String getName() {
		return name;
	}
}
