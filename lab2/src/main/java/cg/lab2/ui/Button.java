package cg.lab2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Button extends JButton implements ActionListener {

    protected Button() {
        setSize(new Dimension(64, 64));
        setBorderPainted(false);
        setBorder(null);
        setContentAreaFilled(false);
    }
}
