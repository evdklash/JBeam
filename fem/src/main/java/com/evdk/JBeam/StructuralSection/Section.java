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
package com.evdk.JBeam.StructuralSection;

/**
 *
 * @author evdklash
 */
public class Section {

    private final String name;
    private final double area;
    private final double Ixx;

    /**
     * Creates a new Section object that provides information required for the
     * calculation of the element's stiffness in bending and compression. In the
     * case of a 2D frame element the area and moment of inertia about the axis
     * of bending is required. In most instances this would be the strong axis
     * of the cross section, typically referred to as \(I_{xx}\)
     *
     * @param name A unique the identifier of the section
     * @param area The cross section area of the section
     * @param Ixx The moment of inertia of the section
     */
    public Section(String name, double area, double Ixx) {
        this.area = area;
        this.name = name;
        this.Ixx = Ixx;
    }

    /**
     *
     * @return The identifier / name of the section
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The cross sectional area
     */
    public double getArea() {
        return area;
    }

    /**
     *
     * @return The second moment of area or otherwise known as the moment of
     * inertia for the axis of bending. For the plane frame analysis this is
     * normally the strong axis moment of inertia, \(I_{xx}\)
     */
    public double getMomentOfInertia() {
        return Ixx;
    }
}
