package sss.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Saeid Dadkhah on 2016-06-25 2:26 AM.
 * Project: SaeidSearchSystem
 */
public class ClassifierEvaluator {

    private NaïveBayesClassifier naïveBayesClassifier;

    private int[] tp;
    private int[] fp;
    private int[] fn;
    private int[] tn;

    public ClassifierEvaluator(int[][] matrix, ArrayList<Integer> documentClasses, int numOfClasses, double trainToTestRatio) {
        int trainSize = (int) (matrix.length * trainToTestRatio);

        // Reform train inputs
        int[][] trainMatrix = new int[trainSize][];
        ArrayList<Integer> trainDocumentClasses = new ArrayList<>();

        HashSet<Integer> hs = new HashSet<>();
        Random r = new Random();
        int rand;
        for (int i = 0; i < trainSize; i++) {
            rand = r.nextInt(documentClasses.size());
            while (hs.contains(rand))
                rand = r.nextInt(documentClasses.size());
            hs.add(rand);
        }

        Iterator<Integer> iterator = hs.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            int next = iterator.next();
            trainMatrix[counter++] = matrix[next];
            trainDocumentClasses.add(documentClasses.get(next));
        }

        // Train classifier
        naïveBayesClassifier = new NaïveBayesClassifier(trainMatrix, trainDocumentClasses, numOfClasses);

        // Evaluate classifier
        tp = new int[numOfClasses];
        fp = new int[numOfClasses];
        fn = new int[numOfClasses];
        tn = new int[numOfClasses];

        for (int i = 0; i < documentClasses.size(); i++) {
            if (hs.contains(i))
                continue;
            int c = naïveBayesClassifier.classify(matrix[i]);
            if (c == documentClasses.get(i)) {
                tp[c]++;
                for (int j = 0; j < numOfClasses; j++)
                    if (j != c)
                        tn[j]++;
            } else {
                fp[c]++;
                fn[documentClasses.get(i)]++;
                for (int j = 0; j < numOfClasses; j++)
                    if (j != c && j != documentClasses.get(i))
                        tn[j]++;
            }
        }
    }

    public NaïveBayesClassifier getNaïveBayesClassifier() {
        return naïveBayesClassifier;
    }

    public double getPrecision(int i) {
        return (double) tp[i] / (tp[i] + fp[i]);
    }

    public double getRecall(int i) {
        return (double) tp[i] / (tp[i] + fn[i]);
    }

    public double getF1Score(int i) {
        double p = getPrecision(i);
        double r = getRecall(i);
        return 2 * p * r / (p + r);
    }

}
