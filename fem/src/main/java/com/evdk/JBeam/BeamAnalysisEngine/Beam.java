/*
 * Copyright 2018 Etienne.
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
package com.evdk.JBeam.BeamAnalysisEngine;

import com.evdk.JBeam.StructuralSection.*;

/**
 *
 * @author Etienne
 */
public abstract class Beam implements LineElement {
    
    private final Section sec;
    private final Station start, end;
    
    public Beam(Section section, Station start, Station end) {
        this.sec = section;
        this.start = start;
        this.end = end;
    }
    
    public Section getSection() {
        return sec;
    }
    
    public Station getStartStation() {
        return start;
    }
    
    public Station getEndStation() {
        return end;
    }
    
}
