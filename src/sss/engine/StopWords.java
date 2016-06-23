package sss.engine;

import java.io.*;
import java.util.*;

public class StopWords {

    public static final int PERSIAN = 0;
    public static final int ENGLISH = 1;

    private static final String PERSIAN_ADDRESS = ".\\files\\PSW.txt";
    private static final String ENGLISH_ADDRESS = ".\\files\\ESW.txt";

    /*
    public static final int M_HASH_SET = 0;
    public static final int M_TREE_SET = 1;
    public static final int M_LINKED_HASH_SET = 2;
    public static final int M_HASH_MAP = 3;
    public static final int M_ARRAY_LIST = 4;
    private int mode;
    */

    private Set<String> set;
    /*
    private HashMap<String, Integer> hashMap;
    private ArrayList<String> arrayList;
    */

    /*
    private Random rand;
    */

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {

            long start = System.currentTimeMillis();
            StopWords psw = new StopWords(PERSIAN);
            //(M_HASH_SET);

//        psw.printAll();

//            System.out.println(psw.isStopWord("بله"));
            long end = System.currentTimeMillis();
            System.out.println("adding stop words:\n\ttime: " + (end - start) + " i: " + i);

            Scanner s = null;
            try {
                s = new Scanner(new File(".//files//Phase.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            PrintStream ps = null;
            try {
                ps = new PrintStream(new FileOutputStream(".\\files\\output" + i + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            start = System.currentTimeMillis();
            String tmp;
            assert s != null;
            while (s.hasNext()) {
                tmp = s.next();
                if (!psw.isStopWord(tmp)) {
                    assert ps != null;
                    System.out.println("\"" + tmp + "\" should be printed");
//					ps.print(tmp + " ");
                }
            }
            end = System.currentTimeMillis();
            System.out.println("writing output:\n\ttime: " + (end - start) + " i: " + i);
        }
    }

    public StopWords(int language) {
        //(int mode) {
        /*
        this.mode = mode;

        switch (mode) {
            case M_HASH_SET:
            */
        set = new HashSet<>();
        /*
                break;
            case M_TREE_SET:
                set = new TreeSet<>();
                break;
            case M_LINKED_HASH_SET:
                set = new LinkedHashSet<>();
                break;
            case M_HASH_MAP:
                hashMap = new HashMap<>();
                break;
            case M_ARRAY_LIST:
                arrayList = new ArrayList<>();
                break;
            default:
                throw new IllegalArgumentException();
        }

        rand = new Random();
        */

        // Added later

        BufferedReader br = null;
        try {
            switch (language) {
                case PERSIAN:
                    br = new BufferedReader(new FileReader(PERSIAN_ADDRESS));
                    break;
                case ENGLISH:
                    br = new BufferedReader(new FileReader(ENGLISH_ADDRESS));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert br != null;
            while (br.ready()) {
                String str = br.readLine();
                addStopWord(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addStopWord(String stopWord) {
        /*
        switch (mode) {
            case M_HASH_SET:
            case M_TREE_SET:
            case M_LINKED_HASH_SET:
            */
        if (set.contains(stopWord))
            return false;
        set.add(stopWord);
        return true;
        /*
            case M_HASH_MAP:
                if (hashMap.containsValue(stopWord))
                    return false;
                hashMap.put(stopWord, rand.nextInt());
                return true;
            case M_ARRAY_LIST:
                if (arrayList.contains(stopWord))
                    return false;
                arrayList.add(stopWord);
                return true;
            default:
                return false;
        }
        */
    }

    public boolean isStopWord(String word) {
        /*
        switch (mode) {
            case M_HASH_SET:
            case M_TREE_SET:
            case M_LINKED_HASH_SET:
           */
        return set.contains(word);
        /*
            case M_HASH_MAP:
                return hashMap.containsKey(word);
            case M_ARRAY_LIST:
                return arrayList.contains(word);
            default:
                return false;
        }
        */
    }

	/*
    public void printAll() {
		switch (mode) {
			case M_HASH_SET:
			case M_TREE_SET:
			case M_LINKED_HASH_SET:
				set.forEach(System.out::println);
				break;
			case M_HASH_MAP:
				for (Map.Entry me : hashMap.entrySet())
					System.out.println(me.getKey() + ": " + me.getValue());
				break;
			case M_ARRAY_LIST:
				for (String s : arrayList)
					System.out.println(s);
				break;
		}
	}*/

}