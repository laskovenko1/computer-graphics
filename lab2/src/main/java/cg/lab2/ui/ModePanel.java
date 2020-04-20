package cg.lab2.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

class ModePanel extends JPanel implements ActionListener, ChangeListener {

    private final MainWindow mainWindow;

    private final JLabel glAlphaTestLabel;
    private final JComboBox<String> glAlphaTestComboBox;
    private final JSlider glAlphaTestSlider;
    private final JLabel glBlendLabel;
    private final JComboBox<String> glBlendSFactorComboBox;
    private final JComboBox<String> glBlendDFactorComboBox;
    private final JLabel glScissorTestLabel;
    private final JSlider glScissorTestXSlider;
    private final JSlider glScissorTestYSlider;

    ModePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        glAlphaTestLabel = new JLabel("GL_ALPHA_TEST");
        glAlphaTestComboBox = new JComboBox<>(new String[]{"GL_NEVER", "GL_LESS", "GL_EQUAL", "GL_LEQUAL", "GL_GREATER",
                "GL_NOTEQUAL", "GL_GEQUAL", "GL_ALWAYS"});
        glAlphaTestComboBox.setSelectedItem("GL_ALWAYS");
        glAlphaTestComboBox.addActionListener(this);
        glAlphaTestSlider = new JSlider(0, 100, 100);
        glAlphaTestSlider.addChangeListener(this);
        glBlendLabel = new JLabel("GL_BLEND");
        glBlendSFactorComboBox = new JComboBox<>(new String[]{"GL_ZERO", "GL_ONE", "GL_SRC_COLOR", "GL_ONE_MINUS_SRC_COLOR",
                "GL_DST_COLOR", "GL_ONE_MINUS_DST_COLOR", "GL_SRC_ALPHA", "GL_ONE_MINUS_SRC_ALPHA", "GL_DST_ALPHA",
                "GL_ONE_MINUS_DST_ALPHA", "GL_CONSTANT_COLOR", "GL_ONE_MINUS_CONSTANT_COLOR", "GL_CONSTANT_ALPHA",
                "GL_ONE_MINUS_CONSTANT_ALPHA", "GL_SRC_ALPHA_SATURATE"});
        glBlendSFactorComboBox.setSelectedItem("GL_ONE");
        glBlendSFactorComboBox.addActionListener(this);
        glBlendDFactorComboBox = new JComboBox<>(new String[]{"GL_ZERO", "GL_ONE", "GL_SRC_COLOR", "GL_ONE_MINUS_SRC_COLOR",
                "GL_DST_COLOR", "GL_ONE_MINUS_DST_COLOR", "GL_SRC_ALPHA", "GL_ONE_MINUS_SRC_ALPHA", "GL_DST_ALPHA",
                "GL_ONE_MINUS_DST_ALPHA", "GL_CONSTANT_COLOR", "GL_ONE_MINUS_CONSTANT_COLOR", "GL_CONSTANT_ALPHA",
                "GL_ONE_MINUS_CONSTANT_ALPHA"});
        glBlendDFactorComboBox.setSelectedItem("GL_ONE");
        glBlendDFactorComboBox.addActionListener(this);
        glScissorTestLabel = new JLabel("GL_SCISSOR_TEST");
        glScissorTestXSlider = new JSlider(0, 600, 600);
        glScissorTestXSlider.addChangeListener(this);
        glScissorTestYSlider = new JSlider(0, 600, 600);
        glScissorTestYSlider.addChangeListener(this);

        setVisible(false);
        setBorder(BorderFactory.createTitledBorder("Set OpenGL mode:"));
        createLayout();
    }

    private void createLayout() {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glAlphaTestLabel)
                        .addComponent(glAlphaTestComboBox)
                        .addComponent(glAlphaTestSlider))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glBlendLabel)
                        .addComponent(glBlendSFactorComboBox)
                        .addComponent(glBlendDFactorComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glScissorTestLabel)
                        .addComponent(glScissorTestXSlider)
                        .addComponent(glScissorTestYSlider)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glAlphaTestLabel)
                        .addComponent(glBlendLabel)
                        .addComponent(glScissorTestLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glAlphaTestComboBox)
                        .addComponent(glBlendSFactorComboBox)
                        .addComponent(glScissorTestXSlider))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(glAlphaTestSlider)
                        .addComponent(glBlendDFactorComboBox)
                        .addComponent(glScissorTestYSlider)));
        this.setLayout(layout);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainWindow.setOptions(Objects.requireNonNull(glAlphaTestComboBox.getSelectedItem()).toString(), glAlphaTestSlider.getValue(),
                Objects.requireNonNull(glBlendSFactorComboBox.getSelectedItem()).toString(), Objects.requireNonNull(glBlendDFactorComboBox.getSelectedItem()).toString(),
                glScissorTestXSlider.getValue(), glScissorTestYSlider.getValue());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        mainWindow.setOptions(Objects.requireNonNull(glAlphaTestComboBox.getSelectedItem()).toString(), glAlphaTestSlider.getValue(),
                Objects.requireNonNull(glBlendSFactorComboBox.getSelectedItem()).toString(), Objects.requireNonNull(glBlendDFactorComboBox.getSelectedItem()).toString(),
                glScissorTestXSlider.getValue(), glScissorTestYSlider.getValue());
    }
}
