package cg.lab1.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

abstract class Button extends JButton implements ActionListener {

    protected final Panel panel;

    protected Button(Panel panel) {
        this.panel = panel;

        setSize(new Dimension(64, 64));
        setBorderPainted(false);
        setBorder(null);
        setContentAreaFilled(false);
    }
}
