package org.sorter.util;

import java.util.List;
import java.util.ArrayList;

public class MathUtil {
    public static double calculateStandardDeviation(List<String> input) {
        List<Double> doubleList = convertStringListToDoubleList(input);
        double mean = calculateMean(doubleList);
        double sumSquaredDiff = calculateSumSquaredDifferences(doubleList, mean);
        return Math.sqrt(sumSquaredDiff / doubleList.size());
    }

    private static List<Double> convertStringListToDoubleList(List<String> input) {
        List<Double> doubleList = new ArrayList<>();
        for (String str : input) {
            try {
                doubleList.add(Double.parseDouble(str));
            } catch (NumberFormatException e) {
                // Handle non-numeric strings if needed
                System.err.println("Non-numeric value found: " + str);
            }
        }
        return doubleList;
    }

    private static double calculateMean(List<Double> doubleList) {
        double sum = 0;
        for (double num : doubleList) {
            sum += num;
        }
        return sum / doubleList.size();
    }

    private static double calculateSumSquaredDifferences(List<Double> doubleList, double mean) {
        double sumSquaredDiff = 0;
        for (double num : doubleList) {
            double diff = num - mean;
            sumSquaredDiff += diff * diff;
        }
        return sumSquaredDiff;
    }
}
