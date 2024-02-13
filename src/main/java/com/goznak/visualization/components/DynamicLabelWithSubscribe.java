package com.goznak.visualization.components;

import javax.swing.*;
import java.awt.*;

public class DynamicLabelWithSubscribe extends JPanel {
    JLabel dynamicLabel = new JLabel();
    public DynamicLabelWithSubscribe(String name) {
        super(new FlowLayout());
        JLabel staticLabel = new JLabel(name);
        add(staticLabel);
        add(dynamicLabel);
    }
    public void setText(String text){
        dynamicLabel.setText(text);
    }
}
