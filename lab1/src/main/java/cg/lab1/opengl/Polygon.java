package cg.lab1.opengl;

import cg.lab1.models.Color4f;
import cg.lab1.models.Pair;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;
import static com.jogamp.opengl.GL2GL3.GL_FILL;

final class Polygon extends Primitive {

    Polygon() {
        super(Primitives.GL_POLYGON);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        gl.glLineWidth(6.0f);
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        gl.glBegin(GL2.GL_POLYGON);
        setPoints();
        gl.glEnd();
    }

    @Override
    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        LinkedList<Pair<Point2D.Float, Color4f>> points = new LinkedList<>();
        points.add(new Pair<>(new Point2D.Float(0.6f, 0.6f), new Color4f(0.5f, 0.1f, 0.1f, 0.2f)));
        points.add(new Pair<>(new Point2D.Float(0.6f, 0.9f), new Color4f(0.1f, 0.9f, 0.8f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.1f, 0.7f), new Color4f(0.1f, 0.2f, 0.8f, 0.4f)));
        points.add(new Pair<>(new Point2D.Float(-0.5f, -0.5f), new Color4f(0.9f, 0.8f, 0.8f, 0.6f)));
        return points;
    }
}
