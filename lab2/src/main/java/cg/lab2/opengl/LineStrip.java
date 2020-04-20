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
import static cg.lab2.opengl.Primitives.GL_LINE_STRIP;

final class LineStrip extends Primitive {

    LineStrip() {
        super(GL_LINE_STRIP);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glLineWidth(4.0f);
        enableMode();

        gl.glBegin(GL.GL_LINE_STRIP);
        setPoints();
        gl.glEnd();
        disableMode();
    }

    @Override
    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        LinkedList<Pair<Point2D.Float, Color4f>> points = new LinkedList<>();
        points.add(new Pair<>(new Point2D.Float(0.6f, 0.6f), new Color4f(0.5f, 0.1f, 0.1f, 0.2f)));
        points.add(new Pair<>(new Point2D.Float(-0.8f, -0.9f), new Color4f(0.1f, 0.9f, 0.8f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.6f, 0.9f), new Color4f(0.1f, 0.9f, 0.8f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(0.2f, -0.2f), new Color4f(0.9f, 0.8f, 0.8f, 0.6f)));
        return points;
    }
}
