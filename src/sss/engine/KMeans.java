package sss.engine;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Saeid Dadkhah on 2016-06-03 2:18 AM.
 * Project: SaeidSearchSystem
 */
public class KMeans {

    private double[][] matrix;
    private int[] clusterNum;
    private int numOfClusters;

    private double[][] centroids;

    public static void main(String[] args) {
        double[][] matrix = {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {10, 10}, {10, 9}, {9, 10}, {9, 9}};
        KMeans kMeans = new KMeans(matrix, 2, 10);
        for (double[] c : kMeans.centroids) {
            for (double d : c)
                System.out.print(d + " ");
            System.out.println();
        }
    }

    public KMeans(double[][] matrix, int numOfClusters, int firstIterations) {
        if (matrix.length == 0)
            throw new IllegalArgumentException();
        this.matrix = matrix;
        this.numOfClusters = numOfClusters;

        initCentroids();
        setCentroids();
        for (int i = 0; i < firstIterations; i++) {
            updateCentroids();
            setCentroids();
        }
    }

    private void initCentroids() {
        centroids = new double[numOfClusters][matrix[0].length];
        clusterNum = new int[matrix.length];
        Random random = new Random();
        int vector[] = new int[numOfClusters];
        for (int i = 0; i < numOfClusters; i++) {
            vector[i] = random.nextInt(matrix.length);
            for (int j = 0; j < i; j++)
                if (vector[j] == vector[i]) {
                    vector[i] = random.nextInt(matrix.length);
                    j = -1;
                }
        }

        for (int i = 0; i < numOfClusters; i++) {
            System.arraycopy(matrix[vector[i]], 0, centroids[i], 0, matrix[vector[i]].length);
        }
//        System.out.println("centroids!");
//        for (int i = 0; i < numOfClusters; i++) {
//            System.out.print(vector[i] + ": ");
//            for (int j = 0; j < matrix[vector[i]].length; j++) {
//                centroids[i][j] = matrix[vector[i]][j];
//                System.out.print(matrix[vector[i]][j] + ", ");
//            }
//            System.out.println();
//        }
    }

    public void update() {
        updateCentroids();
        setCentroids();
    }

    private void setCentroids(){
        for (int i = 0; i < matrix.length; i++)
            clusterNum[i] = closestCentroid(matrix[i]);
    }

    private void updateCentroids() {
        int[] numOfFollowers = new int[numOfClusters];

        // Set centroids and number of their followers to zero initially.
        for (int i = 0; i < numOfClusters; i++) { // Centroids loop
            numOfFollowers[i] = 0;
            for (int j = 0; j < centroids[i].length; j++) // Dimensions loop
                centroids[i][j] = 0;
        }
        for (int i = 0; i < matrix.length; i++) { // Docs loop
            numOfFollowers[clusterNum[i]] += 1;
            for (int j = 0; j < matrix[i].length; j++) { // Dimensions loop
                centroids[clusterNum[i]][j] += matrix[i][j];
            }
        }
        // Calculate Average
        for (int i = 0; i < centroids.length; i++) {
            for (int j = 0; j < centroids[i].length; j++) {
                centroids[i][j] /= numOfFollowers[i];
            }
        }
    }

    public int closestCentroid(double[] vector) {
        int centroid = -1;
        double d = Double.MAX_VALUE;
        for (int i = 0; i < numOfClusters; i++) { // Centroids loop
            double tmp = 0;
            for (int j = 0; j < vector.length; j++) { // Dimensions loop
                tmp += Math.pow(vector[j] - centroids[i][j], 2);
            }
            if (tmp < d) {
                d = tmp;
                centroid = i;
            }
        }
        return centroid;
    }

    public ArrayList<Integer> getDocs(double[] matrix){
        ArrayList<Integer> docIds = new ArrayList<>();
        int centroid = closestCentroid(matrix);
        for(int i = 0; i < clusterNum.length; i++)
            if(clusterNum[i] == centroid)
                docIds.add(i);
        return docIds;
    }

}