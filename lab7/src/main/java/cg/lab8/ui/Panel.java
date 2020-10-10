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
            if (keyEvent.getKeyCode() == KeyEvent.VK_X && !keyEvent.isControlDown()) {
                scene.moveLight(1f, 0f, 0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_X && keyEvent.isControlDown()) {
                scene.moveLight(-1f, 0f, 0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Y && !keyEvent.isControlDown()) {
                scene.moveLight(0f, 1f, 0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Y && keyEvent.isControlDown()) {
                scene.moveLight(0f, -1f, 0f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Z && !keyEvent.isControlDown()) {
                scene.moveLight(0f, 0f, 1f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_Z && keyEvent.isControlDown()) {
                scene.moveLight(0f, 0f, -1f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
                scene.changeProjection();
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_B) {
                scene.changeColor("background");
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_M) {
                scene.changeColor("mirror");
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                scene.changeColor("diffuse");
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                scene.changeMirrorIntensity(-0.1f);
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                scene.changeMirrorIntensity(0.1f);
            }

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

}
