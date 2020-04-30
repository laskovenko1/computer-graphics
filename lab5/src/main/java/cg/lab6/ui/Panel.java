package cg.lab6.ui;

import cg.lab6.opengl.Shader;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;

import static com.jogamp.opengl.GLProfile.GL2;
import static com.jogamp.opengl.GLProfile.get;

class Panel extends GLJPanel {

    Panel() {
        GLProfile profile = get(GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        setRequestedGLCapabilities(capabilities);
        setBounds(new Rectangle(610, 610));
        setMinimumSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));
        Shader shader = new Shader();
        this.addGLEventListener(shader);

        final FPSAnimator animator = new FPSAnimator(this, 30);
        animator.start();
    }
}
