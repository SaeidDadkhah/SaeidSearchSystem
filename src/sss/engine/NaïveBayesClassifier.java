package sss.engine;

import java.util.ArrayList;

/**
 * Created by Saeid Dadkhah on 2016-06-24 1:59 AM.
 * Project: SaeidSearchSystem
 */
public class NaïveBayesClassifier {

    double[][] condProb;
    double[] prior;

    public NaïveBayesClassifier(int[][] matrix, ArrayList<Integer> documentClass, int numOfClasses) {
        int[][] tct = new int[matrix[0].length][numOfClasses];
        condProb = new double[matrix[0].length][numOfClasses];
        prior = new double[numOfClasses];
        // Calculating number of docs in each class in prior
        for (int i = 0; i < documentClass.size(); i++)
            prior[documentClass.get(i)]++;

        // Calculating Tct
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                tct[j][documentClass.get(i)] += matrix[i][j];

        for (int i = 0; i < numOfClasses; i++) {
            prior[i] /= matrix.length;

        }
    }

}
