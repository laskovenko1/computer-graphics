package cg.lab2.opengl;

import cg.lab1.models.Color4f;
import cg.lab1.models.Pair;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static cg.lab2.opengl.Primitives.GL_LINES;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;

final class Lines extends Primitive {

    Lines() {
        super(GL_LINES);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glLineWidth(3.0f);
        enableMode();

        gl.glBegin(GL.GL_LINES);
        setPoints();
        gl.glEnd();
        disableMode();
    }

    @Override
    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        LinkedList<Pair<Point2D.Float, Color4f>> points = new LinkedList<>();
        points.add(new Pair<>(new Point2D.Float(0.8f, 0.9f), new Color4f(0.1f, 0.1f, 0.7f, 0.58f)));
        points.add(new Pair<>(new Point2D.Float(-0.8f, -0.9f), new Color4f(0.9f, 0.9f, 0.9f, 0.58f)));
        points.add(new Pair<>(new Point2D.Float(-0.8f, -0.2f), new Color4f(0.557f, 0.4f, 0.2f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.8f, -0.2f), new Color4f(0.557f, 0.4f, 0.2f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.5f, -0.8f), new Color4f(0.9f, 0.6f, 0.5f, 0.9f)));
        points.add(new Pair<>(new Point2D.Float(-0.5f, 0.8f), new Color4f(0.9f, 0.6f, 0.5f, 0.9f)));
        return points;
    }
}