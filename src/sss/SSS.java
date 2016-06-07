package sss;

import sss.gui.SSS_GUI;
import sss.gui.SplashPage;

/**
 * Created by Saeid Dadkhah on 2016-03-14 5:19 PM.
 * Project: SSS
 */
public class SSS {

    public static final int MODE_LUCENE = 0;
    public static final int MODE_INDEX = 1;
    public static final int MODE_SEARCH = 2;

    public static final int MIN_DOC_LENGTH = 10;
    public static final int MAX_HITS = 10;
    private SSS_Interface spssi;

    public static void main(String[] args){
        new SSS(MODE_INDEX);
    }

    public SSS(int mode){
        SplashPage sp = new SplashPage(500);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        spssi = new SSS_Interface(MODE_INDEX);
        spssi.addDocSaeid("files/Sample.txt");
        spssi.finishIndexing();
        sp.closeSP();
//        SSS_GUI spss_gui =
                new SSS_GUI(spssi, mode);
    }

}
