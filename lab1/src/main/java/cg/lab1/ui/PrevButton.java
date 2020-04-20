package cg.lab1.ui;

import cg.lab1.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

public class PrevButton extends Button {

    public PrevButton(Panel panel) {
        super(panel);

        setToolTipText("Previous");
        URL prevURL = Application.class.getClassLoader().getResource("prev.png");
        setIcon(new ImageIcon(Objects.requireNonNull(prevURL)));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.drawPrevPrimitive();
    }
}
