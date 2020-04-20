package cg.lab2.ui;

import cg.lab2.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

class PrevButton extends Button {

    private final Panel panel;

    PrevButton(Panel panel) {
        this.panel = panel;

        setToolTipText("Previous primitive");
        URL prevURL = Application.class.getClassLoader().getResource("prev.png");
        setIcon(new ImageIcon(Objects.requireNonNull(prevURL)));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.drawPrevPrimitive();
    }
}
