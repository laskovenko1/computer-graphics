package cg.lab3.ui;

import cg.lab2.ui.Button;
import cg.lab3.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

class DrawButton extends Button {

    private final Panel panel;

    DrawButton(Panel panel) {
        this.panel = panel;

        setToolTipText("Draw fractal");
        URL nextURL = Application.class.getClassLoader().getResource("draw.png");
        setIcon(new ImageIcon(Objects.requireNonNull(nextURL)));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.draw();
    }
}
