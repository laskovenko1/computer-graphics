package cg.lab1.ui;

import cg.lab1.opengl.Primitive;
import cg.lab1.opengl.PrimitiveFactory;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.sun.scenario.effect.Color4f;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.List;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

public class Panel extends GLJPanel {

    public static final int LEFT_CODE = 37;
    public static final int RIGHT_CODE = 39;

    private final Label label;
    private final PrimitiveFactory factory;
    private Primitive curPrimitive;

    public Panel(Label label) {
        this.label = label;
        factory = new PrimitiveFactory();
        curPrimitive = factory.getCurPrimitive();

        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(600, 300));

        addGLEventListener(curPrimitive);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case LEFT_CODE:
                        drawPrevPrimitive();
                        break;
                    case RIGHT_CODE:
                        drawNextPrimitive();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        display();
        repaint();
        label.setText(curPrimitive.getName());
    }

    void enableEditMode() {
        List<Pair<Point2D.Float, Color4f>> points = curPrimitive.getPoints();
        for (Pair<Point2D.Float, Color4f> point : points) {
            JButton draggable = new JButton();
            draggable.setSize(5, 5);
//            draggable.setLocation(this.getWidth() *);
        }
    }

    void drawNextPrimitive() {
        drawPrimitive(factory.getNextPrimitive());
    }

    void drawPrevPrimitive() {
        drawPrimitive(factory.getPrevPrimitive());
    }

    private void drawPrimitive(Primitive primitive) {
        GLEventListener listener = getGLEventListener(0);
        removeGLEventListener(listener);
        curPrimitive = primitive;
        addGLEventListener(curPrimitive);
        display();
        label.setText(curPrimitive.getName());
    }
}
