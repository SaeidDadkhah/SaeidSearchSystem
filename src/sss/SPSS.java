package sss;

import sss.gui.SPSS_GUI;
import sss.gui.SplashPage;

/**
 * Created by Saeid Dadkhah on 2016-03-14 5:19 PM.
 * Project: SPSS
 */
public class SPSS {

    private SPSS_Interface spssi;

    public static void main(String[] args){
        new SPSS();
    }

    public SPSS(){
        SplashPage sp = new SplashPage(500);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        spssi = new sss.SPSS_Interface();
        spssi.addDoc("./files/comp.sys.ibm.pc.hardware");
        spssi.finishIndexing();
        sp.closeSP();
//        SPSS_GUI spss_gui =
                new SPSS_GUI(spssi);
    }

}
