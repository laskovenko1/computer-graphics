package cg.lab6.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bolt implements GLEventListener {
    private float attenuation = 0.5f;
    private boolean perspectiveProjection = false;
    private boolean axisMode = false;
    private boolean transparentMode = false;
    private float vertexCount = 8;
    private float yAxisRotation = 4.2f;
    private float xAxisRotation = -23.0f;
    private float xStretching = 0.0f;
    private float yStretching = 0.0f;
    private float zStretching = 0.0f;
    private float scale = 0.5f;
    private List<float[]> hatFront = new ArrayList<>();
    private List<float[]> hatBack = new ArrayList<>();
    private List<float[]> legFront = new ArrayList<>();
    private List<float[]> legBack = new ArrayList<>();
    private List<float[]> threadFront = new ArrayList<>();
    private List<float[]> threadBack = new ArrayList<>();

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        generateVertexes();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glViewport(0, 0, 600, 600);

        final GLU glu = GLU.createGLU(gl);
        setLight(gl);
        if(perspectiveProjection) {
            //перспективная
            gl.glMatrixMode(GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluPerspective(45f,0.75f,0.7f,100.0f);

        } else {
            //ортогональная
            gl.glMatrixMode(GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(-1f, 1f, -1f, 1f, 0.7f, 100f);
        }
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(1 * Math.sin(xAxisRotation) * Math.cos(yAxisRotation),
                1 * Math.sin(yAxisRotation),
                1 * Math.cos(xAxisRotation) * Math.cos(yAxisRotation),
                0, 0, 0,
                0, 1, 0);

        gl.glScaled(scale, scale, scale);

        gl.glDisable(GL_LIGHTING);
        if (axisMode) {
            gl.glLineWidth(0.5f);
            gl.glBegin(GL_LINES);
            gl.glColor4f(1.0f, .0f, .0f, 0.8f);
            gl.glVertex3f(-1.0f, 0.0f, 0.0f);
            gl.glVertex3f(1.0f, 0.0f, 0.0f);
            gl.glColor4f(.0f, 1.0f, .0f, 0.8f);
            gl.glVertex3f(0.0f, -1.0f, 0.0f);
            gl.glVertex3f(0.0f, 1.0f, 0.0f);
            gl.glColor4f(.0f, .0f, 1.0f, 0.8f);
            gl.glVertex3f(0.0f, 0.0f, -1.0f);
            gl.glVertex3f(0.0f, 0.0f, 1.0f);
            gl.glEnd();
        }

        setLight(gl);
        gl.glPolygonMode(GL_FRONT, GL_FILL);
        //шапочка
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = (int) vertexCount - 1; i >= 0; --i) {
            gl.glVertex3f(hatFront.get(i)[0], hatFront.get(i)[1], hatFront.get(i)[2]);
        }
        gl.glEnd();

        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(hatBack.get(i)[0], hatBack.get(i)[1], hatBack.get(i)[2]);
        }
        gl.glEnd();

        //ножка
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(legBack.get(i)[0], legBack.get(i)[1], legBack.get(i)[2]);
        }
        gl.glEnd();

        //резьба
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(threadBack.get(i)[0], threadBack.get(i)[1], threadBack.get(i)[2]);
        }
        gl.glEnd();

        // шапочка соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(hatBack.get(i)[0], hatBack.get(i)[1], hatBack.get(i)[2]);
            gl.glVertex3f(hatFront.get(i)[0], hatFront.get(i)[1], hatFront.get(i)[2]);
        }
        gl.glVertex3f(hatBack.get(0)[0], hatBack.get(0)[1], hatBack.get(0)[2]);
        gl.glVertex3f(hatFront.get(0)[0], hatFront.get(0)[1], hatFront.get(0)[2]);
        gl.glEnd();

        //ножка соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(legBack.get(i)[0], legBack.get(i)[1], legBack.get(i)[2]);
            gl.glVertex3f(legFront.get(i)[0], legFront.get(i)[1], legFront.get(i)[2]);
        }
        gl.glVertex3f(legBack.get(0)[0], legBack.get(0)[1], legBack.get(0)[2]);
        gl.glVertex3f(legFront.get(0)[0], legFront.get(0)[1], legFront.get(0)[2]);
        gl.glEnd();

        //резьба соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < (int) vertexCount; ++i) {
            gl.glVertex3f(threadBack.get(i)[0], threadBack.get(i)[1], threadBack.get(i)[2]);
            gl.glVertex3f(threadFront.get(i)[0], threadFront.get(i)[1], threadFront.get(i)[2]);
        }
        gl.glVertex3f(threadBack.get(0)[0], threadBack.get(0)[1], threadBack.get(0)[2]);
        gl.glVertex3f(threadFront.get(0)[0], threadFront.get(0)[1], threadFront.get(0)[2]);
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void setUpRotations(float xAxisRotation, float yAxisRotation) {
        System.out.println(xAxisRotation + " "+ yAxisRotation);
        this.xAxisRotation = xAxisRotation;
        this.yAxisRotation = yAxisRotation;
    }

    public void setUpBreaking(float vertexCount) {
        this.vertexCount += vertexCount;
        if (this.vertexCount < 2.0f || this.vertexCount > 20.0f) {
            this.vertexCount -= vertexCount;
        }
        generateVertexes();
    }

    public void setUpScale(float scale) {
        this.scale += scale;
        System.out.println(this.scale);
        if(this.scale < 0.1) {
            this.scale = 0.1f;
        }
        if(this.perspectiveProjection && this.scale > 0.3) {
            this.scale = 0.3f;
        }
        if(!this.perspectiveProjection && this.scale > 1.0) {
            this.scale = 1.0f;
        }
    }

    public void setAxisMode() {
        this.axisMode = !axisMode;
    }

    public void setTransparentMode() {
        this.transparentMode = !transparentMode;
    }

    public void setUpAttenuation(float attenuation) {
        this.attenuation += attenuation;
        if (this.attenuation < 0.0f || this.attenuation > 10.0f) {
            this.attenuation -= attenuation;
        }
    }

    public void setUpProjection() {
        this.perspectiveProjection = !this.perspectiveProjection;
        setUpScale(0.1f);
    }

    public void setUpStretching(float xStretching, float yStretching, float zStretching) {
        this.xStretching += xStretching;
        this.yStretching += yStretching;
        this.zStretching += zStretching;
        if(this.xStretching > 1.0f || this.xStretching < -0.1f ||
                this.yStretching > 1.0f || this.yStretching < -0.1f ||
                this.zStretching > 1.0f || this.zStretching < -0.0f) {
            this.xStretching -= xStretching;
            this.yStretching -= yStretching;
            this.zStretching -= zStretching;
        }
        generateVertexes();
    }

    private void stretchVertexes(List<float[]> vertexList) {
        for(int i = 0; i < (int)vertexCount/2; i++) {
            vertexList.get(i)[0] += xStretching;
            vertexList.get(i)[1] += yStretching;
        }
        for(int j = (int)vertexCount/2; j < vertexCount; j++) {
            vertexList.get(j)[0] -= xStretching;
            vertexList.get(j)[1] -= yStretching;
        }
    }

    private void generateVertexes() {
        float hatFrontZ = 0.5f + this.zStretching;
        float hatBackZ = 0.5f - 0.2f - zStretching;
        float legBackZ = hatBackZ - 0.5f - zStretching;
        float threadBackZ = legBackZ - 0.4f - zStretching;
        generatePolygon(0.5f, hatFrontZ, hatFront);
        stretchVertexes(hatFront);
        generatePolygon(0.5f, hatBackZ, hatBack);
        stretchVertexes(hatBack);
        //generate Leg
        generatePolygon(0.22f, hatBackZ, legFront);
        stretchVertexes(legFront);
        generatePolygon(0.22f, legBackZ, legBack);
        stretchVertexes(legBack);
        //generate thread
        generatePolygon(0.18f, legBackZ, threadFront);
        stretchVertexes(threadFront);
        generatePolygon(0.18f, threadBackZ, threadBack);
        stretchVertexes(threadBack);
    }

    private void generatePolygon(float radius, float z, List<float[]> vertexList) {
        vertexList.clear();
        for (int i = 0; i < vertexCount; i++) {
            float a = (float) i / vertexCount * 3.1415f * 2.0f;
            vertexList.add(new float[]{(float) cos(a) * radius, (float) sin(a) * radius, z});
        }
    }

    private void setLight(GL2 gl) {
        float[] diffuse = {1.0f, .949019608f, .929411765f};
        float[] position = {.3f, 1.0f, 1.0f, 1.0f};

        float[] materialFrontShininess = {10.0f};
        float[] materialFrontDiffuse = {.333333333f, 0.411764706f, .478431373f, transparentMode ? .5f : 1.0f};

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);

        gl.glDepthFunc(GL_LEQUAL);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(diffuse));
        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(position));

        gl.glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, attenuation);
        gl.glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 0.2f);
        gl.glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.1f);

        gl.glMaterialfv(GL_FRONT, GL_SHININESS, FloatBuffer.wrap(materialFrontShininess));
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, FloatBuffer.wrap(materialFrontDiffuse));
    }
}
