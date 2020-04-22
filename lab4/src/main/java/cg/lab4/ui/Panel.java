package cg.lab4.ui;

import cg.lab4.model.ControlPoint;
import cg.lab4.model.Point;
import cg.lab4.opengl.NurbSpline;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Optional;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    private final NurbSpline nurbSpline = new NurbSpline();

    private ControlPoint selected;

    Panel() {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(610, 610));
        setMinimumSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));

        addMouseListener(new MouseButtonHandler());
        addMouseMotionListener(new MouseMotionHandler());
        this.addGLEventListener(nurbSpline);
    }

    private float normalizeX(int x) {
        return (float) x / getWidth();
    }

    private float normalizeY(int y) {
        return (float) y / getHeight();
    }

    private Optional<ControlPoint> findPoint(int x, int y) {
        return nurbSpline.getControlPoints().stream()
                .filter(cp -> cp.getPoint() != null)
                .filter(cp -> cp.getPoint().isPoint(normalizeX(x), normalizeY(y)))
                .findAny();
    }

    private class MouseButtonHandler implements MouseListener {

        public static final int RIGHT_MOUSE_BUTTON = 3;

        @Override
        public void mousePressed(MouseEvent e) {
            Optional<ControlPoint> optionalSelected = findPoint(e.getX(), e.getY());
            if (optionalSelected.isPresent()) {
                selected = optionalSelected.get();
                return;
            }

            Optional<ControlPoint> invisiblePoint = nurbSpline.getControlPoints().stream()
                    .filter(p -> p.getPoint() == null)
                    .findAny();
            invisiblePoint.ifPresent(controlPoint -> controlPoint.setPoint(new Point(normalizeX(e.getX()), normalizeY(e.getY()))));

            Panel.this.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selected = null;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() != RIGHT_MOUSE_BUTTON)
                return;
            Optional<ControlPoint> optionalControlPoint = findPoint(e.getX(), e.getY());
            if (!optionalControlPoint.isPresent())
                return;

            PopUp menu = new PopUp(optionalControlPoint.get());
            menu.setSize(170, 50);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        private class PopUp extends JPopupMenu {

            public static final double MINIMUM_VALUE = 0.1d;
            public static final double MAXIMUM_VALUE = 3.0d;
            public static final double STEP_SIZE = 0.05d;

            public PopUp(ControlPoint controlPoint) {
                SpinnerNumberModel model = new SpinnerNumberModel(controlPoint.getWeight(), MINIMUM_VALUE, MAXIMUM_VALUE, STEP_SIZE);
                JSpinner weight = new JSpinner(model);
                weight.addChangeListener(e -> {
                    JSpinner source = (JSpinner) e.getSource();
                    Double currentValue = (Double) source.getValue();
                    controlPoint.setWeight(currentValue.floatValue());
                    Panel.this.repaint();
                });
                add(weight);
            }
        }
    }

    private class MouseMotionHandler implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selected == null)
                return;

            selected.setPoint(new Point(normalizeX(e.getX()), normalizeY(e.getY())));
            Panel.this.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}
