package cg.lab2.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.sun.scenario.effect.Color4f;
import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static cg.lab2.opengl.Primitives.GL_TRIANGLE_STRIP;

final class TriangleStrip extends Primitive {

    TriangleStrip() {
        super(GL_TRIANGLE_STRIP);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glLineWidth(3.0f);
        gl.glPolygonMode(GL_BACK, GL_LINE);
        enableMode();

        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        setPoints();
        gl.glEnd();
        disableMode();
    }

    @Override
    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        LinkedList<Pair<Point2D.Float, Color4f>> points = new LinkedList<>();
        points.add(new Pair<>(new Point2D.Float(-0.8f, -0.2f), new Color4f(0.8f, 0.5f, 0.2f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.6f, 0.1f), new Color4f(0.8f, 0.5f, 0.2f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.4f, -0.2f), new Color4f(0.8f, 0.5f, 0.2f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.2f, 0.1f), new Color4f(0.2f, 0.8f, 0.5f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0, -0.2f), new Color4f(0.5f, 0.2f, 0.8f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.2f, 0.1f), new Color4f(0.6f, 0.6f, 0.6f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.4f, -0.2f), new Color4f(0.2f, 0.4f, 0.6f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.6f, 0.1f), new Color4f(0.7f, 0.4f, 0.3f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.8f, -0.2f), new Color4f(0.7f, 0.9f, 0.3f, 0.4f)));
        return points;
    }
}
