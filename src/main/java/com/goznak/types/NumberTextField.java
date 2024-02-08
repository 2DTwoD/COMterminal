package com.goznak.types;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

public class NumberTextField extends JTextField {
    public NumberTextField(int maxValue) {
        super();
        getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            try{
                Integer.parseInt(getText());
            }
            catch (Exception ex){
                try {
                    setText(getText(0, e.getLength()));
                } catch (BadLocationException exc) {
                    throw new RuntimeException(exc);
                }
            }

        });

    }
}
