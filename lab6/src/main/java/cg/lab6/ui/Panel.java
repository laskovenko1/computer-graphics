package cg.lab6.ui;

import cg.lab6.opengl.Bolt;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import java.awt.*;
import java.awt.event.*;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    private Bolt bolt;
    private int posX;
    private int posY;
    private float xAxisRotation = 0;
    private float yAxisRotation = 0;

    Panel() {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(585, 620));
        setMinimumSize(new Dimension(585, 600));
        setMaximumSize(new Dimension(585, 600));

        bolt = new Bolt();
        this.addGLEventListener(bolt);

        addMouseListener(new MouseButtonHandler());
        addMouseMotionListener(new MouseMotionHandler());
        addMouseWheelListener(new MouseWheelHandler());
        addKeyListener(new KeyboardButtonHandler());
    }

    private class MouseButtonHandler implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            posX = e.getX();
            posY = e.getY();
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
            xAxisRotation += (180 * ((float) e.getY() - (float) posY)) / (getHeight());
            yAxisRotation += (180 * ((float) e.getX() - (float) posX)) / (getWidth());

            posX = e.getX();
            posY = e.getY();

            bolt.setUpRotations(xAxisRotation, yAxisRotation);
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    private class MouseWheelHandler implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            float scale = (float) -e.getWheelRotation() / 10;
            bolt.setUpScale(scale);
            repaint();
        }
    }

    private class KeyboardButtonHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_X && !keyEvent.isControlDown()) {
                bolt.setUpStretching(0.1f, 0.0f, 0.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_X && keyEvent.isControlDown()) {
                bolt.setUpStretching(-0.1f, 0.0f, 0.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Y && !keyEvent.isControlDown()) {
                bolt.setUpStretching(0.0f, 0.1f, 0.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Y && keyEvent.isControlDown()) {
                bolt.setUpStretching(0.0f, -0.1f, 0.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Z && !keyEvent.isControlDown()) {
                bolt.setUpStretching(0.0f, 0.0f, 0.1f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Z && keyEvent.isControlDown()) {
                bolt.setUpStretching(0.0f, 0.0f, -0.1f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                bolt.setUpBreaking(1.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                bolt.setUpBreaking(-1.0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                bolt.setUpAttenuation(-0.2f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                bolt.setUpAttenuation(+0.2f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
                bolt.setAxisMode();
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_T) {
                bolt.setTransparentMode();
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
                bolt.setUpProjection();
            }
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

}
