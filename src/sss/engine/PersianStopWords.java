package sss.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Saeid Dadkhah on 2016-06-21 2:42 AM.
 * Project: SaeidSearchSystem
 */
public class PersianStopWords {

    private static final String stopWordsAddress = ".\\files\\PSW.txt";

    private StopWords stopWords;

    public PersianStopWords() {
        stopWords = new StopWords(StopWords.M_HASH_MAP);


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(stopWordsAddress));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert br != null;
            while (br.ready()) {
                String str = br.readLine();
                stopWords.addStopWord(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStopWord(String word) {
        return stopWords.isStopWord(word);
    }
}
