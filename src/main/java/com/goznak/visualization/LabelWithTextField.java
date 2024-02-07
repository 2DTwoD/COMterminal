package com.goznak.visualization;

import javax.swing.*;

public class LabelWithTextField extends JPanel {
    private final JTextField textField = new JTextField(10);
    public LabelWithTextField(String labelText, int maxSymbolInTextField) {
        super();
        JLabel label = new JLabel();
        label.setText(labelText);
        textField.setColumns(maxSymbolInTextField);
        add(label);
        add(textField);
        SwingUtilities.invokeLater(this::revalidate);
        SwingUtilities.invokeLater(this::repaint);
    }

    public String getText(){
        return textField.getText();
    }

    public void setText(String value){
        textField.setText(value);
    }
}
