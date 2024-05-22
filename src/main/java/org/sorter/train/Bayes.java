package org.sorter.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bayes {

    // Training data
    private final Map<Integer, List<String>> dataMap;
    // Class prior probabilities
    private double cPP_Y;
    private double cPP_N;

    // Conditional probabilities
    private final Map<String, Double> trained;

    private double meanDensityY, meanDensityN, stdDevDensityY, stdDevDensityN;
    private double meanSugarY, meanSugarN, stdDevSugarY, stdDevSugarN;

    public Bayes(Map<Integer, List<String>> dataMap) {
        this.dataMap = dataMap;
        this.trained = new HashMap<>();
    }

    public void train() {
        int totalSamples = dataMap.size();
        int countY = 0; // Count of 'Yes' instances
        int countN = 0; // Count of 'No' instances

        List<Double> densitiesY = new ArrayList<>();
        List<Double> densitiesN = new ArrayList<>();
        List<Double> sugarsY = new ArrayList<>();
        List<Double> sugarsN = new ArrayList<>();

        // Count the occurrences of each class and collect density and sugar values
        for (List<String> list : dataMap.values()) {
            String label = list.get(list.size() - 1); // Last element is the label
            double density = Double.parseDouble(list.get(list.size() - 3));
            double sugar = Double.parseDouble(list.get(list.size() - 2));

            if ("1".equals(label)) {
                countY++;
                densitiesY.add(density);
                sugarsY.add(sugar);
            } else {
                countN++;
                densitiesN.add(density);
                sugarsN.add(sugar);
            }
        }

        // Calculate class prior probabilities
        cPP_Y = (double) countY / totalSamples;
        cPP_N = (double) countN / totalSamples;

        // Compute means and standard deviations
        meanDensityY = calculateMean(densitiesY);
        meanDensityN = calculateMean(densitiesN);
        stdDevDensityY = calculateStandardDeviation(densitiesY, meanDensityY);
        stdDevDensityN = calculateStandardDeviation(densitiesN, meanDensityN);

        meanSugarY = calculateMean(sugarsY);
        meanSugarN = calculateMean(sugarsN);
        stdDevSugarY = calculateStandardDeviation(sugarsY, meanSugarY);
        stdDevSugarN = calculateStandardDeviation(sugarsN, meanSugarN);

        // Compute conditional probabilities for non-numeric features
        computeConditionalProbabilities(countY, countN);
    }

    private void computeConditionalProbabilities(int countY, int countN) {
        Map<String, Integer> featureCountYMap = new HashMap<>();
        Map<String, Integer> featureCountNMap = new HashMap<>();

        // Count occurrences of features for each class
        for (List<String> list : dataMap.values()) {
            String label = list.get(list.size() - 1); // Last element is the label
            List<String> features = list.subList(0, list.size() - 3); // Features excluding label and numeric features

            Map<String, Integer> featureCountMap = "1".equals(label) ? featureCountYMap : featureCountNMap;

            for (String feature : features) {
                featureCountMap.put(feature, featureCountMap.getOrDefault(feature, 0) + 1);
            }
        }

        // Calculate conditional probabilities
        for (Map.Entry<String, Integer> entry : featureCountYMap.entrySet()) {
            double probY = (double) entry.getValue() / countY;
            trained.put(entry.getKey() + "|Y", probY);
        }

        for (Map.Entry<String, Integer> entry : featureCountNMap.entrySet()) {
            double probN = (double) entry.getValue() / countN;
            trained.put(entry.getKey() + "|N", probN);
        }
    }

    private double calculateMean(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private double calculateStandardDeviation(List<Double> values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.size());
    }

    private double gaussianProbability(double x, double mean, double stdDev) {
        return (1 / (Math.sqrt(2 * Math.PI) * stdDev)) * Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(stdDev, 2)));
    }

    public String predict(List<String> features) {
        double probY = cPP_Y;
        double probN = cPP_N;

        for (String feature : features) {
            probY *= trained.getOrDefault(feature + "|Y", 1.0 / (cPP_Y + 2.0));
            probN *= trained.getOrDefault(feature + "|N", 1.0 / (cPP_N + 2.0));
        }

        // Get density and sugar values from features (assuming they are the last two in the list)
        double density = Double.parseDouble(features.get(features.size() - 2));
        double sugar = Double.parseDouble(features.get(features.size() - 1));

        // Calculate probabilities for density and sugar
        probY *= gaussianProbability(density, meanDensityY, stdDevDensityY);
        probN *= gaussianProbability(density, meanDensityN, stdDevDensityN);
        probY *= gaussianProbability(sugar, meanSugarY, stdDevSugarY);
        probN *= gaussianProbability(sugar, meanSugarN, stdDevSugarN);

        return probY > probN ? "1" : "0"; // Returning labels directly for simplicity
    }

}
