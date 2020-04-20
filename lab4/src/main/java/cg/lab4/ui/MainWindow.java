package cg.lab4.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final Panel panel;

    public MainWindow() {
        this.panel = new Panel();

        setBounds(new Rectangle(660, 680));
        setSize(new Dimension(630, 650));
        setMaximumSize(new Dimension(700, 700));
        setResizable(false);
        setTitle("OpenGL NURBS curve");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getMaximumSize().width) / 2);
        int y = (int) ((dimension.getHeight() - getMaximumSize().height) / 2);
        setLocation(x, y);

        setLayout();
    }

    private void setLayout() {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(panel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(panel));
        this.setLayout(layout);
    }
}
