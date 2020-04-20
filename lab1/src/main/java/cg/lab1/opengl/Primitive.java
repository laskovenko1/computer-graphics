package cg.lab1.opengl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.sun.scenario.effect.Color4f;
import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class Primitive implements GLEventListener {

    private final Primitives type;
    protected GL2 gl;

    public Primitive(Primitives type) {
        this.type = type;
    }

    public String getName() {
         return type.toString();
    }

    public List<Pair<Point2D.Float, Color4f>> getPoints() {
        throw new UnsupportedOperationException();
    }

    public Pair<Point2D.Float, Color4f> findPoint(float x, float y) {
        return getPoints().stream()
                .filter(p -> Float.compare(p.getKey().x, x) == 0 && Float.compare(p.getKey().y, y) == 0)
                .findAny()
                .orElse(null);
    }

    void setPoints() {
        for (Pair<Point2D.Float, Color4f> pair : getPoints()) {
            Point2D.Float point = pair.getKey();
            Color4f color = pair.getValue();
            gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            gl.glVertex2d(point.x, point.y);
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}
