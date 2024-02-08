package com.goznak.visualization;

import com.goznak.types.NumberTextField;

import javax.swing.*;
import javax.swing.text.Document;

public class LabelWithNumberTextField extends JPanel {
    private final NumberTextField textField;
    public LabelWithNumberTextField(String labelText, int maxValue) {
        super();
        JLabel label = new JLabel();
        label.setText(labelText);
        textField = new NumberTextField(maxValue);
        textField.setColumns(10);
        add(label);
        add(textField);
    }

    public String getText(){
        return textField.getText();
    }

    public void setText(String value){
        textField.setText(value);
    }
    public Document getDocument(){
        return textField.getDocument();
    }
}
