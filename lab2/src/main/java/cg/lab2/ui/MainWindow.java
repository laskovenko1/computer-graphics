package cg.lab2.ui;

import com.jogamp.opengl.GL;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final cg.lab2.ui.Panel panel;
    private final cg.lab2.ui.Label label;
    private final PrevButton prevButton;
    private final NextButton nextButton;
    private final EditButton editButton;
    private final ModePanel modePanel;

    public MainWindow() {
        this.label = new Label();
        this.panel = new Panel(label);
        this.prevButton = new PrevButton(panel);
        this.nextButton = new NextButton(panel);
        this.editButton = new EditButton(this);
        this.modePanel = new ModePanel(this);

        init();
        createLayout();
    }

    public void open() {
        setVisible(true);
    }

    void editMode() {
        if (!modePanel.isVisible()) {
            setSize(new Dimension(780, 900));
        } else {
            setSize(new Dimension(780, 780));
        }
        modePanel.setVisible(!modePanel.isVisible());
    }

    void setOptions(String glAlphaTestComboBoxItem, int glAlphaTestSliderValue,
                    String glBlendSFactorComboBoxItem, String glBlendDFactorComboBoxItem,
                    int glScissorTestXSliderValue, int glScissorTestYSliderValue) {
        try {
            int glAlphaTestFunc = GL.class.getField(glAlphaTestComboBoxItem).getInt(null);
            int glBlendSFactor = GL.class.getField(glBlendSFactorComboBoxItem).getInt(null);
            int glBlendDFactor = GL.class.getField(glBlendDFactorComboBoxItem).getInt(null);
            panel.getCurPrimitive().setModeOptions(glAlphaTestFunc, (float) glAlphaTestSliderValue / 100.0f,
                    glBlendSFactor, glBlendDFactor,
                    glScissorTestXSliderValue, glScissorTestYSliderValue);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        panel.display();
    }

    private void init() {
        setBounds(new Rectangle(800, 1000));
        setSize(new Dimension(780, 780));
        setMaximumSize(new Dimension(780, 980));
        setTitle("OpenGL primitives");
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
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(prevButton)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(label)
                        .addComponent(panel)
                        .addComponent(editButton)
                        .addComponent(modePanel))
                .addComponent(nextButton));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(prevButton)
                        .addComponent(panel)
                        .addComponent(nextButton))
                .addComponent(editButton)
                .addComponent(modePanel));
        this.setLayout(layout);
    }
}
