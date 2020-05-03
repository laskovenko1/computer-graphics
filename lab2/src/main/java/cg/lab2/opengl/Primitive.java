package cg.lab2.opengl;

import cg.lab1.models.Color4f;
import cg.lab1.models.Pair;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class Primitive implements GLEventListener {

    private final Primitives type;
    protected GL2 gl;
    protected int glAlphaTestFunc = GL.GL_ALWAYS;
    protected float glAlphaTestRef = 1.0f;
    protected int glBlendSFactor = GL.GL_ONE;
    protected int glBlendDFactor = GL.GL_ONE;
    protected int glScissorTestX = 600;
    protected int glScissorTestY = 600;

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

    public void setModeOptions(int glAlphaTestFunc, float glAlphaTestRef,
                               int glBlendSFactor, int glBlendDFactor,
                               int glScissorTestX, int glScissorTestY) {
        this.glAlphaTestFunc = glAlphaTestFunc;
        this.glAlphaTestRef = glAlphaTestRef;
        this.glBlendSFactor = glBlendSFactor;
        this.glBlendDFactor = glBlendDFactor;
        this.glScissorTestX = glScissorTestX;
        this.glScissorTestY = glScissorTestY;
    }

    void enableMode() {
        gl.glEnable(GL2.GL_ALPHA_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_SCISSOR_TEST);
        gl.glAlphaFunc(glAlphaTestFunc, glAlphaTestRef);
        gl.glBlendFunc(glBlendSFactor, glBlendDFactor);
        gl.glScissor(0, 0, glScissorTestX, glScissorTestY);
    }

    void disableMode() {
        gl.glDisable(GL2.GL_ALPHA_TEST);
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_SCISSOR_TEST);
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
