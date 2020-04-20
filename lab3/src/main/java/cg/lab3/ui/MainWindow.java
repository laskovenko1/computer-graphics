package cg.lab3.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final cg.lab3.ui.Panel panel;
    private final DrawButton drawButton;
    private final StopButton stopButton;

    public MainWindow() {
        this.panel = new Panel();
        this.drawButton = new DrawButton(panel);
        this.stopButton = new StopButton(panel);

        init();
        createLayout();
    }

    private void init() {
        setBounds(new Rectangle(650, 740));
        setSize(new Dimension(640, 730));
        setMaximumSize(new Dimension(640, 730));
        setTitle("OpenGL IFS fractal");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Center window on screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getMaximumSize().width) / 2);
        int y = (int) ((dimension.getHeight() - getMaximumSize().height) / 2);
        setLocation(x, y);
    }

    private void createLayout() {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(panel)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(drawButton)
                        .addGap(100)
                        .addComponent(stopButton)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(panel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(drawButton)
                        .addComponent(stopButton)));
        this.setLayout(layout);
    }
}
