package cg.lab1.opengl;

import com.jogamp.opengl.GLAutoDrawable;
import com.sun.scenario.effect.Color4f;
import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;

final class Points extends Primitive {

    Points() {
        super(Primitives.GL_POINTS);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glPointSize(10.0f);

        gl.glBegin(GL_POINTS);
        setPoints();
        gl.glEnd();
    }

    @Override
    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        LinkedList<Pair<Point2D.Float, Color4f>> points = new LinkedList<>();
        points.add(new Pair<>(new Point2D.Float(-0.5f, -0.5f), new Color4f(0.1f, 0.3f, 0.5f, 0.2f)));
        points.add(new Pair<>(new Point2D.Float(-0.1f, 0.6f), new Color4f(0.35f, 0.26f, 0.65f, 0.8f)));
        points.add(new Pair<>(new Point2D.Float(-0.1f, 0.2f), new Color4f(0.65f, 0.32f, 0.54f, 0.6f)));
        points.add(new Pair<>(new Point2D.Float(-0.9f, 0.1f), new Color4f(0.54f, 0.1f, 0.2f, 0.35f)));
        points.add(new Pair<>(new Point2D.Float(0.6f, -0.5f), new Color4f(0.02f, 0.76f, 0.22f, 0.56f)));
        return points;
    }
}
