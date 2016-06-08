package sss.gui.component;

import sss.engine.LuceneEngineFields;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Saeid Dadkhah on 2016-03-30 3:09 AM.
 * Project: SSS
 */
public class Key extends JComboBox<String> {

    public Key() {
        setEditable(true);

        Key.this.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                updateItems();
            }
        });

        updateItems();
    }

    private void updateItems() {
        String typed = getText();
        Key.this.removeAllItems();
        Key.this.addItem(typed);

        for (int i = 0; i < LuceneEngineFields.NUM_OF_FIELDS; i++) {
            String str = LuceneEngineFields.getName(i);
            if (str != null && str.startsWith(typed)) {
                Key.this.addItem(LuceneEngineFields.getName(i));
            }
        }
    }

    public String getText() {
        return ((JTextComponent) getEditor().getEditorComponent()).getText();
    }

}
