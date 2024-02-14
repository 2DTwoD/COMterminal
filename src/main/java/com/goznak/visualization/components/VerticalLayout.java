package com.goznak.visualization.components;

import javax.swing.*;
import java.awt.*;

public class VerticalLayout implements LayoutManager {
    final private Dimension size = new Dimension();
    final private JComponent panel;
    private int distanceX;
    private int distanceY;
    private int paddingY;
    final int align;
    static final public int LEFT = 0;
    static final public int CENTER = 1;
    public VerticalLayout(JComponent panel, int align){
        this.panel = panel;
        this.align = align;
    }
    public VerticalLayout(JComponent panel, int align, int paddingY){
        this(panel, align);
        this.paddingY = paddingY;
    }
    public VerticalLayout(JComponent panel, int distanceX, int distanceY, int align){
        this(panel, align);
        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }
    public VerticalLayout(JComponent panel, int distanceX, int distanceY, int align, int paddingY){
        this(panel, align, paddingY);
        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }
    @Override
    public void addLayoutComponent(String name, Component comp) {
        panel.revalidate();
        panel.repaint();
    }
    @Override
    public void removeLayoutComponent(Component comp) {
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    @Override
    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    @Override
    public void layoutContainer(Container container) {
        Component[] list = container.getComponents();
        int currentX = distanceX;
        if (align == CENTER)
            currentX = panel.getSize().width / 2;

        int currentY = distanceY + paddingY;
        int shiftingX = 0;
        for (Component component : list) {
            Dimension pref = component.getPreferredSize();
            if (align == CENTER)
                shiftingX = pref.width / 2;
            component.setBounds(currentX - shiftingX, currentY, pref.width, pref.height);
            currentY += distanceY + paddingY;
            currentY += pref.height;
        }
        container.revalidate();
    }

    private Dimension calculateBestSize(Container c){
        Component[] list = c.getComponents();
        int maxWidth = 0;
        for (Component component : list) {
            int width = component.getWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        size.width = maxWidth;
        int height = 0;
        for (Component component : list) {
            height += distanceY + paddingY;
            height += component.getHeight();
        }
        size.height = height;
        return size;
    }
}
