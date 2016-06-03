package sss.engine;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by Saeid Dadkhah on 2016-06-01 2:33 PM.
 * Project: SSS
 */
public class Dictionary {

    private HashMap<String, Integer> hMDic;

    public Dictionary() {
        hMDic = new HashMap<>();
    }

    public boolean add(String word, Integer id) {
        if (!hMDic.containsKey(word)) {
            hMDic.put(word, id);
            return true;
        }
        return false;
    }

    public Integer getId(String word) {
        return hMDic.get(word);
    }

    public int genNewId() {
        Random random = new Random();
        Integer rand = random.nextInt();
        while (hMDic.containsValue(rand))
            rand = random.nextInt();
        return rand;
    }

    public Set<String> keySet(){
        return hMDic.keySet();
    }

}
