package cg.lab4.opengl;

import cg.lab4.model.Point;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;

public class ControlPoints implements GLEventListener {

    private GL2 gl;
    private Point point;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (point == null)
            return;

        //gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        gl.glPointSize(10.0f);
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);

        gl.glPushMatrix();
        gl.glTranslatef(-1.0f, 1.0f, 0);
        gl.glScalef(2.0f, -2.0f, 0);
        gl.glBegin(GL_POINTS);
        gl.glColor3f(0, 1.0f, 0);
        gl.glVertex2d(point.getX(), point.getY());
        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}
