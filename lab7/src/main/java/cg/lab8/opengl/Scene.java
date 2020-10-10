package cg.lab8.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class Scene implements GLEventListener {
    private static final float ROTATION_STEP = (float) Math.PI / 72;
    private float xRotation = 0;
    private float zRotation = 0;

    private List<float[]> colorList = new ArrayList<>();
    private int backgroundColorIndex;
    private int mirrorColorIndex;
    private int diffuseColorIndex;
    private float[] lightPosition = {-10.0f, 5.0f, 1.0f, 0.5f};
    private float[] mirrorIntensity = {0.5f, 0.1f, 0.1f, 0.1f};
    private float[] sphereParams = {0.03f, 30.0f, 30.0f};
    private boolean perspectiveProjection = false;

    private GL2 gl;
    private GLUT glut;
    private GLU glu;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        glut = new GLUT();
        glu = GLU.createGLU(gl);
        initColor();
        backgroundColorIndex = 0;
        mirrorColorIndex = 0;
        diffuseColorIndex = 0;
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        setLight(gl);
        setMaterial(gl);
        gl.glPushMatrix();
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

        float radius = 10.0f;
        float camX = (float) Math.sin(xRotation) * radius;
        float camZ = (float) Math.cos(zRotation) * radius;
        glu.gluLookAt(camX, 1.5f, camZ, 0, 0, 0, 0, 1, 0);


        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glPolygonMode(GL_FRONT, GL_FILL);

        gl.glColor3f(0.0f, 0.9f, 0.9f);
        gl.glRotatef(-90, 1f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0f, -0.95f);
        glut.glutSolidCylinder(0.3f, 1.9f, 50, 50);
        gl.glTranslatef(0.0f, 0f, 0.95f);
        gl.glRotatef(90, 1f, 0.0f, 0.0f);

        gl.glColor3f(0.9f, 0.f, 0.0f);
        drawDNALine();

        gl.glColor3f(0.0f, 0.9f, 0.0f);
        gl.glRotatef(180, 0, 1, 0);
        drawDNALine();

        gl.glPopMatrix();
        gl.glDisable(GL_DEPTH_TEST);
    }

    private void drawDNALine() {
        int angle = 4;
        float y = 0.01f;
        for(int angleCoeff = 0, yCoeff = 95; yCoeff >= -95 ; angleCoeff++, yCoeff -= 2) {
            gl.glRotatef(angle * angleCoeff, 0, 1, 0);
            gl.glTranslatef(0f, yCoeff * y, 0.7f);
            glut.glutSolidSphere(sphereParams[0], (int)sphereParams[1], (int)sphereParams[2]);
            gl.glTranslatef(0f, - yCoeff * y, -0.7f);
            gl.glRotatef(-angle * angleCoeff, 0, 1, 0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void changeMirrorIntensity(float intensity) {
        for (int i = 0; i < mirrorIntensity.length; i++) {
            if ((intensity < 0 && mirrorIntensity[i] > 0.2f) || (intensity > 0 && mirrorIntensity[i] < 1.0f)) {
                mirrorIntensity[i] += intensity;
            }
        }
        System.out.println(mirrorIntensity[0]);
    }

    public void changeProjection() {
        perspectiveProjection = !perspectiveProjection;
    }

    public void moveLight(float x, float y, float z) {
        lightPosition[0] += x;
        lightPosition[1] += y;
        lightPosition[2] += z;
    }

    public void changeColor(String type) {
        switch (type) {
            case "mirror":
                mirrorColorIndex = ((mirrorColorIndex + 1) % colorList.size());
                break;
            case "background":
                backgroundColorIndex = ((backgroundColorIndex + 1) % colorList.size());
                break;
            case "diffuse":
                diffuseColorIndex = ((diffuseColorIndex + 1) % colorList.size());
                break;
            default:
        }
    }

    private void setMaterial(GL2 gl) {
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glMaterialfv(GL_FRONT, GL_SHININESS, FloatBuffer.wrap(mirrorIntensity)); //степень отражения
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, FloatBuffer.wrap(colorList.get(backgroundColorIndex))); //цвет отражения
    }

    private void setLight(GL2 gl) {
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);

        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(lightPosition));
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(colorList.get(diffuseColorIndex)));
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(colorList.get(backgroundColorIndex)));
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, FloatBuffer.wrap(colorList.get(mirrorColorIndex)));
    }

    private void initColor() {
        colorList.add(new float[]{0.5f, 0.5f, 0.5f, 0.0f});
        colorList.add(new float[]{1.0f, 0.0f, 0.0f, 0.0f});
        colorList.add(new float[]{1.0f, 0.5f, 0.0f, 0.0f});
        colorList.add(new float[]{1.0f, 1.0f, 0.0f, 0.0f});
        colorList.add(new float[]{0.0f, 1.0f, 0.0f, 0.0f});
        colorList.add(new float[]{0.0f, 0.5f, 0.5f, 0.0f});
        colorList.add(new float[]{0.0f, 0.0f, 1.0f, 0.0f});
        colorList.add(new float[]{0.5f, 0.0f, 1.0f, 0.0f});
    }

    public void rotateRight() {
        xRotation += ROTATION_STEP;
        if (xRotation >= 2 * Math.PI)
            xRotation = 0;

        zRotation += ROTATION_STEP;
        if (zRotation >= 2 * Math.PI)
            zRotation = 0;
    }

    public void rotateLeft() {
        xRotation -= ROTATION_STEP;
        if (xRotation <= 0)
            xRotation = (float) (2 * Math.PI);

        zRotation -= ROTATION_STEP;
        if (zRotation <= 0)
            zRotation = (float) (2 * Math.PI);
    }
}
