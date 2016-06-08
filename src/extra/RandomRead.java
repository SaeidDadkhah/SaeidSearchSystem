package extra;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Saeid Dadkhah on 2016-06-08 1:44 AM.
 * Project: SaeidSearchSystem
 */
public class RandomRead {

    public static void main(String[] args) {
        int cluster = 1;
        int record = 1;
        int bufferSize = 256 * 1024;
        String docSplitter = "<مقاله>";
        char[] buffer = new char[bufferSize];

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(".\\files\\Phase.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert br != null;
            br.skip(1 * bufferSize);
            br.read(buffer);
            String[] tmp = (new String(buffer)).split(docSplitter);
            System.out.println();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br = new BufferedReader(new FileReader(".\\files\\Phase.txt"));
            br.skip(1 * bufferSize);
            br.read(buffer);
            String[] tmp = (new String(buffer)).split(docSplitter);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
