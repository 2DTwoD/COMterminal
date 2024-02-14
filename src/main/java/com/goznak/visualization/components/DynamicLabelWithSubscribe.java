package com.goznak.visualization.components;

import javax.swing.*;
import java.awt.*;

public class DynamicLabelWithSubscribe extends JPanel {
    JLabel dynamicLabel = new JLabel();
    JLabel staticLabel;
    public DynamicLabelWithSubscribe(String name) {
        super(new FlowLayout());
        staticLabel = new JLabel(name);
        add(staticLabel);
        add(dynamicLabel);
    }
    public void setText(String text){
        dynamicLabel.setText(text);
    }
    public void setLabelFont(Font font){
        dynamicLabel.setFont(font);
        staticLabel.setFont(font);
    }
}
