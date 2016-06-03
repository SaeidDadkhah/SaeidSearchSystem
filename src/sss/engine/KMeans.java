package sss.engine;

import java.util.Random;

/**
 * Created by Saeid Dadkhah on 2016-06-03 2:18 AM.
 * Project: SaeidSearchSystem
 */
public class KMeans {

    private int[][] matrix;
    private int numOfClusters;

    private double[][] centroids;

    public static void main(String[] args) {
        int[][] matrix = {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {10, 10}, {10, 9}, {9, 10}, {9, 9}};
        KMeans kMeans = new KMeans(matrix, 2, 10);
        for (double[] c : kMeans.centroids) {
            for (double d : c)
                System.out.print(d + " ");
            System.out.println();
        }
    }

    public KMeans(int[][] matrix, int numOfClusters, int firstIterations) {
        if (matrix.length == 0)
            throw new IllegalArgumentException();
        this.matrix = matrix;
        this.numOfClusters = numOfClusters;

        initCentroids();
        for (int i = 0; i < firstIterations; i++)
            setAndUpdateCentroids();
    }

    private void initCentroids() {
        centroids = new double[numOfClusters][matrix[0].length];
        Random random = new Random();
        int vector[] = new int[numOfClusters];
        for (int i = 0; i < numOfClusters; i++) {
            vector[i] = random.nextInt(matrix.length);
            for (int j = 0; j < i; j++)
                if (vector[j] == vector[i]) {
                    vector[i] = random.nextInt(matrix.length);
                    j = -1;
                    continue;
                }
        }
        System.out.println("centroids!");
        for (int i = 0; i < numOfClusters; i++) {
            System.out.println(vector[i]);
            for (int j = 0; j < matrix[vector[i]].length; j++) {
                centroids[i][j] = matrix[vector[i]][j];
            }
        }
    }

    public void update() {
        setAndUpdateCentroids();
    }

    private void setAndUpdateCentroids() {
        int[] clusterNum = new int[matrix.length];
        int[] numOfFollowers = new int[numOfClusters];
        // Set centroids
        for (int j = 0; j < matrix.length; j++) { // Docs loop
            double d = Double.MAX_VALUE;
            for (int k = 0; k < numOfClusters; k++) { // Centroids loop
                double tmp = 0;
                for (int l = 0; l < matrix[j].length; l++) { // Dimensions loop
                    tmp += Math.pow(matrix[j][l] - centroids[k][l], 2);
                }
                if (tmp < d) {
                    d = tmp;
                    clusterNum[j] = k;
                }
            }
        }

        // Update centroids
        // Set centroids and number of their followers to zero initially.
        for (int j = 0; j < numOfClusters; j++) { // Centroids loop
            numOfFollowers[j] = 0;
            for (int k = 0; k < centroids[j].length; k++) // Dimensions loop
                centroids[j][k] = 0;
        }
        for (int j = 0; j < matrix.length; j++) { // Docs loop
            numOfFollowers[clusterNum[j]] += 1;
            for (int k = 0; k < matrix[j].length; k++) { // Dimensions loop
                centroids[clusterNum[j]][k] += matrix[j][k];
            }
        }
        // Calculate Average
        for (int j = 0; j < centroids.length; j++) {
            for (int k = 0; k < centroids[j].length; k++) {
                centroids[j][k] /= numOfFollowers[j];
            }
        }
    }

}
