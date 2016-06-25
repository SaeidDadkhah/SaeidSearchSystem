package sss.engine;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Saeid Dadkhah on 2016-06-24 1:59 AM.
 * Project: SaeidSearchSystem
 */
public class NaïveBayesClassifier {

    double[][] condProb;
    double[] prior;

    public NaïveBayesClassifier(int[][] matrix, ArrayList<Integer> documentClasses, int numOfClasses) {
        int[][] tct = new int[matrix[0].length][numOfClasses];
        int[] sigmaTCT = new int[numOfClasses];
        condProb = new double[matrix[0].length][numOfClasses];
        prior = new double[numOfClasses];

        // Calculating number of docs in each class in prior
        for (Integer documentClass : documentClasses) prior[documentClass]++;
        System.out.println("N");

        // Calculating prior
        for (int i = 0; i < numOfClasses; i++) {
            prior[i] /= matrix.length;
        }
        System.out.println("Prior");

        // Calculating Tct
        for (int[] arr : tct)
            Arrays.fill(arr, 1);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                tct[j][documentClasses.get(i)] += matrix[i][j];
            }
        }
        System.out.println("Tct");

        // Calculating sigmaTCT
        for (int[] aTct : tct) {
            for (int j = 0; j < aTct.length; j++) {
                sigmaTCT[j] += aTct[j];
            }
        }
        System.out.println("sigmaTct");

        // Calculating condProb
        for (int i = 0; i < condProb.length; i++) {
            for (int j = 0; j < condProb[i].length; j++) {
                condProb[i][j] = (double) tct[i][j] / sigmaTCT[j];
            }
        }
        System.out.println("condProb");
    }

    public int classify(int[] vector) {
        int c = -1;
        double s = Double.NEGATIVE_INFINITY;
        double score;
        for (int i = 0; i < prior.length; i++) {
            score = Math.log(prior[i]);
            for (int j = 0; j < vector.length; j++)
                if (vector[j] > 0)
                    score += Math.log(condProb[j][i]);
            if (score > s) {
                s = score;
                c = i;
            }
        }
        return c;
    }

}
