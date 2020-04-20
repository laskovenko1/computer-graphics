    package cg.lab3.ui;

    import cg.lab2.ui.Button;
    import cg.lab3.Application;

    import javax.swing.*;
    import java.awt.event.ActionEvent;
    import java.net.URL;
    import java.util.Objects;

    class StopButton extends Button {

        private final Panel panel;

        StopButton(Panel panel) {
            this.panel = panel;

            setToolTipText("Stop");
            URL nextURL = Application.class.getClassLoader().getResource("stop.png");
            setIcon(new ImageIcon(Objects.requireNonNull(nextURL)));

            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.clear();
        }
    }
