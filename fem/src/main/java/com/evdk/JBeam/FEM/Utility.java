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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Utility {

    public static void addSubmatrix(int index[], double[][] matrix, double subMatrix[][]) {
        for (int i = 0; i < index.length; i++) {
            for (int j = 0; j < index.length; j++) {
                matrix[index[i]][index[j]] = matrix[index[i]][index[j]] + subMatrix[i][j];
            }
        }
    }

    public static void printMatrix(double[][] matrix) {
        String str = "|\t";
        for (int k = 0; k < matrix.length; k++) {
            for (int m = 0; m < matrix[0].length; m++) {
                str += matrix[k][m] + "\t";
            }
            System.out.println(str + "|");
            str = "|\t";
        }
    }

    public static NumberFormat numberFormat(int MaxFracDigits, int MinFracDigits) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).setDecimalSeparatorAlwaysShown(true);
        }
        formatter.setMaximumFractionDigits(MaxFracDigits);
        formatter.setMinimumFractionDigits(MinFracDigits);
        return formatter;
    }

    public static void printVector(double[] vec, int width) {
        // Allocate memory for the formatted vector
        ArrayList<String> formattedVector = new ArrayList<>();
        // Create a number formatter to print comma separated decimal numbers
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).setDecimalSeparatorAlwaysShown(true);
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(5);
        }
        System.out.println();
        // Get the max length of a number in the vector
        int max = 0;
        for (int i = 0; i < vec.length; i++) {
            String str = formatter.format(vec[i]);
            max = Math.max(max, width);
            max = Math.max(max, str.length());
        }
        // Crete new strings with padding
        for (int i = 0; i < vec.length; i++) {
            String str = formatter.format(vec[i]);
            String formatstring = "%1$" + max + "s";
//            System.out.println(formatstring);
            String newstr = String.format(formatstring, str);
            formattedVector.add(newstr);
        }
        // Print the formatted vector
        for (double el : vec) {
            System.out.print(el + "\n");
        };
        System.out.println();
    }

    public static void printVector(int[] vec, int width) {
        // Allocate memory for the formatted vector
        ArrayList<String> formattedVector = new ArrayList<>();
        // Create a number formatter to print integer numbers
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).setDecimalSeparatorAlwaysShown(false);
        }
        System.out.println();
        // Get the max length of a number in the vector
        int max = 0;
        for (int i = 0; i < vec.length; i++) {
            String str = formatter.format(vec[i]);
            max = Math.max(max, str.length());
        }
        // Crete new strings with padding
        for (int i = 0; i < vec.length; i++) {
            String str = formatter.format(vec[i]);
            String formatstring = "%1$" + max + "s";
//            System.out.println(formatstring);
            String newstr = String.format(formatstring, str);
            formattedVector.add(newstr);
        }
        // Print the formatted vector
        formattedVector.stream().forEach((number) -> {
            System.out.print(number + "\n");
        });
        System.out.println();
    }

}
