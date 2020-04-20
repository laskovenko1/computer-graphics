package cg.lab2.ui;

import cg.lab2.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Objects;

class EditButton extends Button {

    private final MainWindow mainWindow;
    private boolean isEnabled;

    EditButton(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        isEnabled = true;

        changeState();
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        changeState();
        mainWindow.editMode();
    }

    private void changeState() {
        String text;
        String iconName;
        if (isEnabled) {
            text = "Edit mode";
            iconName = "edit.png";
        } else {
            text = "Close edit mode";
            iconName = "closeEdit.png";
        }
        setToolTipText(text);
        URL nextURL = Application.class.getClassLoader().getResource(iconName);
        setIcon(new ImageIcon(Objects.requireNonNull(nextURL)));
        isEnabled = !isEnabled;
    }
}
