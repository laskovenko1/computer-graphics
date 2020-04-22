package cg.lab4.opengl;

import cg.lab4.model.ControlPoint;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLUnurbs;
import com.jogamp.opengl.glu.gl2.GLUgl2;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_ACCUM_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_MAP1_VERTEX_4;

public class NurbSpline implements GLEventListener {

    public static int CONTROL_POINTS_NUMBER = 6;

    private final List<ControlPoint> controlPoints = Collections.unmodifiableList(Stream.generate(ControlPoint::new)
            .limit(CONTROL_POINTS_NUMBER)
            .collect(Collectors.toList()));
    private final float[] knots = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f};

    private GL2 gl;

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        List<ControlPoint> visible = findVisibleControlPoints();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glTranslatef(-1.0f, 1.0f, 0);
        gl.glScalef(2.0f, -2.0f, 0);

        gl.glPointSize(10.0f);
        gl.glBegin(GL_POINTS);
        gl.glColor3f(0, 1.0f, 0);
        visible.forEach(this::displayPoint);
        gl.glEnd();

        gl.glLineWidth(0.5f);
        gl.glBegin(GL_LINE_STRIP);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        visible.forEach(this::displayPoint);
        gl.glEnd();

        if (visible.size() == CONTROL_POINTS_NUMBER) {
            gl.glColor3f(0f, 0f, 1.0f);
            displayNURBS();
        }

        gl.glPopMatrix();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    private List<ControlPoint> findVisibleControlPoints() {
        return controlPoints.stream()
                .filter(controlPoint -> !Objects.isNull(controlPoint.getPoint()))
                .collect(Collectors.toList());
    }

    private void displayPoint(ControlPoint controlPoint) {
        gl.glVertex2d(controlPoint.getPoint().getX(), controlPoint.getPoint().getY());
    }

    private void displayNURBS() {
        GLUgl2 glu = new GLUgl2();

        double[] doubleflatPoints = controlPoints.stream()
                .map(ControlPoint::to4DList)
                .flatMap(Collection::stream)
                .mapToDouble(Float::floatValue)
                .toArray();
        float[] flatPoints = Floats.toArray(Doubles.asList(doubleflatPoints));
        GLUnurbs gluNurbs = glu.gluNewNurbsRenderer();
        glu.gluBeginCurve(gluNurbs);
        glu.gluNurbsCurve(gluNurbs, 10, knots, 4, flatPoints, 4, GL_MAP1_VERTEX_4);
        glu.gluEndCurve(gluNurbs);
    }
}
