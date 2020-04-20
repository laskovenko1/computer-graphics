package cg.lab4.ui;

import cg.lab4.model.Point;
import cg.lab4.opengl.ControlPoints;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    public static int CONTROL_POINTS_NUMBER = 6;

    private final List<ControlPoints> controlPoints = Collections.unmodifiableList(Stream.generate(ControlPoints::new)
            .limit(CONTROL_POINTS_NUMBER)
            .collect(Collectors.toList()));

    Panel() {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(610, 610));
        setMinimumSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));

        addMouseListener(new MouseButtonHandler());
        addMouseMotionListener(new MouseMotionHandler());
        controlPoints.forEach(this::addGLEventListener);
    }

    private float normalizeX(int x) {
        return (float) x / getWidth();
    }

    private float normalizeY(int y) {
        return (float) y / getHeight();
    }

    private Optional<ControlPoints> findPoint(int x, int y) {
        return controlPoints.stream()
                .filter(cp -> cp.getPoint() != null)
                .filter(cp -> cp.getPoint().isPoint(normalizeX(x), normalizeY(y)))
                .findAny();
    }

    private class MouseButtonHandler implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (findPoint(e.getX(), e.getY()).isPresent())
                return;

            Optional<ControlPoints> invisiblePoint = controlPoints.stream()
                    .filter(p -> p.getPoint() == null)
                    .findAny();
            invisiblePoint.ifPresent(controlPoint -> controlPoint.setPoint(new Point(normalizeX(e.getX()), normalizeY(e.getY()))));

            Panel.this.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private class MouseMotionHandler implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            findPoint(e.getX(), e.getY()).ifPresent(controlPoint -> {
                controlPoint.setPoint(new Point(normalizeX(e.getX()), normalizeY(e.getY())));
                Panel.this.repaint();
            });
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}
