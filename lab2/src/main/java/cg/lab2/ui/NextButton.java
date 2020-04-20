package cg.lab2.ui;

import cg.lab2.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

class NextButton extends Button {

    private final Panel panel;

    NextButton(Panel panel) {
        this.panel = panel;

        setToolTipText("Next primitive");
        URL nextURL = Application.class.getClassLoader().getResource("next.png");
        setIcon(new ImageIcon(Objects.requireNonNull(nextURL)));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.drawNextPrimitive();
    }
}
