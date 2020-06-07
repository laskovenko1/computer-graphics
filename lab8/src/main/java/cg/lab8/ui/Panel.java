package cg.lab8.ui;

import cg.lab8.opengl.Scene;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import java.awt.*;
import java.awt.event.*;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    private Scene scene;
    float posX = 0;
    private float yAxisRotation = 0;

    Panel() {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(585, 620));
        setMinimumSize(new Dimension(585, 600));
        setMaximumSize(new Dimension(585, 600));

        scene = new Scene();
        this.addGLEventListener(scene);

        addKeyListener(new KeyboardButtonHandler());
    }


    private class KeyboardButtonHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                scene.rotateLeft();
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                scene.rotateRight();
            }

            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                scene.increaseScale();
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                scene.decreaseScale();
            }

            if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                scene.changeColor();
            }
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }
}
