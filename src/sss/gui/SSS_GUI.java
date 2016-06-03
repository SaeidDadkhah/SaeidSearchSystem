package sss.gui;

import org.apache.lucene.document.Document;
import sss.SSS_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Saeid Dadkhah on 2016-03-14 5:49 PM.
 * Project: SSS
 */
public class SSS_GUI extends JFrame {

    private SSS_Interface SSS_interface;
    private SSS_MainSearch SSS_mainSearch;
    private SSS_AdvancedSearch SSS_advancedSearch;

    public SSS_GUI(SSS_Interface SSS_interface) {
        this.SSS_interface = SSS_interface;

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
        turnToAdvanced();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            SSS_mainSearch.setSize(getSize());
        } catch (NullPointerException ignored) {
        }
    }

    public void search(String query) {
        Document[] res = null;
        try {
            res = SSS_interface.search(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new SSS_Results(res);
    }

    public void turnToMain(boolean remove) {
        if (remove)
            getContentPane().remove(SSS_advancedSearch);
        SSS_mainSearch = new SSS_MainSearch(this);
        SSS_mainSearch.setSize(getSize());
        SSS_mainSearch.setPreferredSize(getSize());
        SSS_mainSearch.setBackground(Color.WHITE);
        SSS_mainSearch.setLocation(0, 0);
        getContentPane().add(SSS_mainSearch);
        this.dispatchEvent(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, null));
    }

    public void turnToAdvanced() {
        getContentPane().remove(SSS_mainSearch);
        SSS_advancedSearch = new SSS_AdvancedSearch(this);
        SSS_advancedSearch.setSize((int) getSize().getWidth() - 6, (int) getSize().getHeight() - 28);
        SSS_advancedSearch.setLocation(0, 0);
        getContentPane().add(SSS_advancedSearch);
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
