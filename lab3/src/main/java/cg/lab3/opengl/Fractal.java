package cg.lab3.opengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import cg.lab3.geom.Point;
import cg.lab3.geom.Triangle;

import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;

public class Fractal implements GLEventListener {

    private final List<Triangle> triangles;
    private GL2 gl;

    public Fractal() {
        triangles = new ArrayList<>();
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glPolygonMode(GL_BACK, GL2.GL_LINE);
        gl.glBegin(GL_TRIANGLES);
        setPoints();
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    private void setPoints() {
        for (Triangle triangle : triangles) {
            Point[] points = triangle.getPoints();
            gl.glColor4f(0, 0.8f, 1.0f, 0);
            gl.glVertex2d(points[0].getX(), points[0].getY());
            gl.glColor4f(1.0f, 1.0f, 1.0f, 0);
            gl.glVertex2d(points[1].getX(), points[1].getY());
            gl.glVertex2d(points[2].getX(), points[2].getY());
        }
    }
}
