package sss;

import sss.gui.SSS_GUI;
import sss.gui.SplashPage;

/**
 * Created by Saeid Dadkhah on 2016-03-14 5:19 PM.
 * Project: SSS
 */
public class SSS {

    private SSS_Interface spssi;

    public static void main(String[] args){
        new SSS();
    }

    public SSS(){
        SplashPage sp = new SplashPage(500);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        spssi = new SSS_Interface();
        spssi.addDoc("./files/comp.sys.ibm.pc.hardware");
        spssi.finishIndexing();
        sp.closeSP();
//        SSS_GUI spss_gui =
                new SSS_GUI(spssi);
    }

}
