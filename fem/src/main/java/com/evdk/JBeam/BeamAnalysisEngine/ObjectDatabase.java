package com.evdk.JBeam.BeamAnalysisEngine;

import java.util.*;
import java.io.*;

public class ObjectDatabase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public HashMap<String, Object> objectMap;
    public String autoFile = null;
    public char[] lastAutoId = {'_', '0', '0', '0', '0', '0', '0', '0'};

//... Constructor .......................................................
    public ObjectDatabase() {
        objectMap = new HashMap<>(1024);
    }

//... Automatic assignment of names to objects ..........................
    public String autoName() {
        String newName;
        for (int i = 1; i < 8; i++) {
            if (setAutoId(i)) {
                newName = new String(lastAutoId);
                return newName;
            }
        }
        System.out.println(" *** Base : too many objects ***");
        return null;
    }

    private boolean setAutoId(int index) {
        char value = lastAutoId[index];
        if (value < '9') {
            lastAutoId[index]++;
        } else if (value == '9') {
            lastAutoId[index] = 'A';
        } else if (value < 'Z') {
            lastAutoId[index]++;
        } else if (value == 'Z') {
            lastAutoId[index] = 'a';
        } else if (value < 'z') {
            lastAutoId[index]++;
        } else if (value == 'z') {
            lastAutoId[index] = '0';
            return false;
        }
        return true;
    }

//... Put an object into the base .......................................
    public String putObject(Object object) {
        String objectName = ((NamedObject) object).getName();
        if (objectMap.containsKey(objectName)) {
            return null;
        }
        objectMap.put(objectName, object);
        return objectName;
    }

//... get an object from the base .......................................
    public Object getObject(String objectName) { // String realName                     ;
        if (objectName == null) {
            return null;
        }
        return objectMap.get(objectName);
    }

//... Check if the base contains an object ..............................
    public boolean containsObject(String objectName) {
        Object object = getObject(objectName);
        if (object != null) {
            return true;
        } else {
            return false;
        }
    }

//... Remove an object from the base ....................................
    public boolean removeObject(String objectName) {
        if (objectName == null) {
            return false;
        }
        if (objectMap.containsKey(objectName)) {
            objectMap.remove(objectName);
            return true;
        } else {
            return false;
        }
    }

//... Clear the data base ...............................................
    public void clear() {
        objectMap.clear();
        for (int i = 1; i < lastAutoId.length; i++) {
            lastAutoId[i] = '0';
        }
    }
}
