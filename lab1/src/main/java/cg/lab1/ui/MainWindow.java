package cg.lab1.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final JPanel panel;
    private final JLabel label;
    private final JButton prevButton;
    private final JButton nextButton;

    public MainWindow(JPanel panel, JLabel label, JButton prevButton, JButton nextButton) {
        this.panel = panel;
        this.label = label;
        this.prevButton = prevButton;
        this.nextButton = nextButton;

        init();
        createLayout();
    }

    public void open() {
        setVisible(true);
    }

    private void init() {
        setResizable(false);
        setBounds(new Rectangle(900, 600));
        setTitle("OpenGL primitives");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Center window on screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    private void createLayout() {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(prevButton)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(label)
                        .addComponent(panel))
                .addComponent(nextButton));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(prevButton)
                        .addComponent(panel)
                        .addComponent(nextButton)));

        this.setLayout(layout);
    }
}
