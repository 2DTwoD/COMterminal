package com.goznak.visualization.components;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

public class ScrolledTextArea extends JScrollPane {
    JTextArea textArea;
    public ScrolledTextArea(int rows, int columns) {
        super();
        textArea  = new JTextArea(rows, columns);
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        setViewportView(textArea);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void insertString(String text){
        try {
            textArea.getDocument().insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
    public String getText(){
        return textArea.getText();
    }
}
