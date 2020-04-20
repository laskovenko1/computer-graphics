package cg.lab1.ui;

import cg.lab1.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

public class NextButton extends Button {

    public NextButton(Panel panel) {
        super(panel);

        setToolTipText("Next");
        URL nextURL = Application.class.getClassLoader().getResource("next.png");
        setIcon(new ImageIcon(Objects.requireNonNull(nextURL)));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.drawNextPrimitive();
    }
}
