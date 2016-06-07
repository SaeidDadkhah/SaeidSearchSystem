package sss.gui;

import sss.SSS_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Saeid Dadkhah on 2016-03-14 5:49 PM.
 * Project: SSS
 */
public class SSS_GUI extends JFrame {

    private SSS_Interface sSS_interface;
    private SSS_MainSearch sSS_mainSearch;
    private SSS_AdvancedSearch sSS_advancedSearch;

    private int mode;

    public SSS_GUI(SSS_Interface sSS_interface, int mode) {
        this.sSS_interface = sSS_interface;
        this.mode = mode;

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
            }
        }

        setTitle("SSS®");
        setLayout(null);

        Dimension ss = getToolkit().getScreenSize();
        setSize(5 * (int) ss.getWidth() / 7, 5 * (int) ss.getHeight() / 7);
        setLocation((int) ss.getWidth() / 7, (int) ss.getHeight() / 7);
        setResizable(false);
        setIconImage(new ImageIcon("./files/mainSearch.png").getImage());

        turnToMain(false);
//        turnToAdvanced();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            sSS_mainSearch.setSize(getSize());
        } catch (NullPointerException ignored) {
        }
    }

    public void search(String query) {
        try {
            sSS_interface.search(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new SSS_Results(res); todo
    }

    public void turnToMain(boolean remove) {
        if (remove)
            getContentPane().remove(sSS_advancedSearch);
        sSS_mainSearch = new SSS_MainSearch(this);
        sSS_mainSearch.setSize(getSize());
        sSS_mainSearch.setPreferredSize(getSize());
        sSS_mainSearch.setBackground(Color.WHITE);
        sSS_mainSearch.setLocation(0, 0);
        getContentPane().add(sSS_mainSearch);
        this.dispatchEvent(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, null));
    }

    public void turnToAdvanced() {
        getContentPane().remove(sSS_mainSearch);
        sSS_advancedSearch = new SSS_AdvancedSearch(this);
        sSS_advancedSearch.setSize((int) getSize().getWidth() - 6, (int) getSize().getHeight() - 28);
        sSS_advancedSearch.setLocation(0, 0);
        getContentPane().add(sSS_advancedSearch);
        this.dispatchEvent(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, null));
    }

    @Override
    protected void processEvent(AWTEvent e) {
        super.processEvent(e);
        switch (e.getID()) {
            case ActionEvent.RESERVED_ID_MAX + 1:
                setSize(getWidth(), getHeight() - 1);
                setSize(getWidth(), getHeight() + 1);
                repaint();
        }
    }
}
