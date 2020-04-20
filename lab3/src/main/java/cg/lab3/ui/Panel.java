package cg.lab3.ui;

import cg.lab3.opengl.Fractal;
import cg.lab3.opengl.FractalDrawer;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import java.awt.*;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    private volatile FractalDrawer drawer;

    private volatile Fractal fractal;
    private volatile boolean isDrawing = false;

    Panel() throws GLException {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(610, 610));
        setMinimumSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));
    }

    synchronized void draw() {
        if (isDrawing) {
            return;
        }
        isDrawing = true;
        drawer = new FractalDrawer();
        fractal = drawer.init();
        addGLEventListener(fractal);
        new Thread(new DrawingChain(this, 13)).start();
    }

    void clear() {
        if (!isDrawing)
            return;
        isDrawing = false;
        removeGLEventListener(fractal);
        fractal = drawer.init();
    }

    private class DrawingChain implements Runnable {

        private final Panel panel;
        private final int length;

        private DrawingChain(Panel panel, int length) {
            this.panel = panel;
            this.length = length;
        }

        @Override
        public void run() {
            if (!isDrawing || length < 0) {
                isDrawing = false;
                clear();
                return;
            }
            if (fractal == null)
                throw new IllegalStateException("Fractal is not initialised");
            panel.display();
            drawer.step();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            new DrawingChain(panel, length - 1).run();
        }
    }
}
