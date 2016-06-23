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

    public static final int MAX_HITS = 10;

    public static void main(String[] args) {
        try {
            if (args.length == 0)
                new SSS(MODE_INDEX, "files/Sample.txt");
            else if (args.length == 1)
                new SSS(MODE_INDEX, args[0]);
//            new SSS(MODE_LUCENE, "./files/comp.sys.ibm.pc.hardware");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SSS(int mode, String fileAddress) throws Exception {
        SplashPage sp = new SplashPage(500);
        SSS_Interface sss_interface = new SSS_Interface(fileAddress, mode);
        sss_interface.addDoc();
        sss_interface.finishIndexing();
        sp.closeSP();
        new SSS_GUI(sss_interface);
    }

}
