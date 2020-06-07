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
    private List<float[]> colorList = new ArrayList<>();
    private int backgroundColorIndex;
    private int mirrorColorIndex;
    private int diffuseColorIndex;
    private int teapotColorIndex;
    private float[] lightPosition = {-10.0f, 5.0f, 1.0f, 0.5f};
    private float[] mirrorIntensity = {0.1f, 0.1f, 0.1f, 0.1f};
    private boolean perspectiveProjection = false;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        initColor();
        backgroundColorIndex = 0;
        mirrorColorIndex = 0;
        diffuseColorIndex = 0;
        teapotColorIndex = 0;
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        final GLUT glut = new GLUT();
        final GLU glu = GLU.createGLU(gl);

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
        glu.gluLookAt(1, 1, 1, 0, 0, 0, 0, 1, 0);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glPolygonMode(GL_FRONT, GL_FILL);
        gl.glColor3f(0.2f, 0.0125f, 0.06f);
        gl.glTranslatef(0f, -0.2f, 0.0f);
        glut.glutSolidCube(0.35f);

        gl.glScaled(1f, 1f, 1f);
        gl.glTranslatef(0f, 0.3f, 0.0f);
        gl.glColor3f(colorList.get(teapotColorIndex)[0],  colorList.get(teapotColorIndex)[1], colorList.get(teapotColorIndex)[2]);
        glut.glutSolidTeapot(0.15f, true);
        gl.glPopMatrix();
        gl.glDisable(GL_DEPTH_TEST);
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
    };

    public void changeProjection() {
        perspectiveProjection = !perspectiveProjection;
    }

    public void moveLight(float x, float y, float z) {
        lightPosition[0] += x;
        lightPosition[1] += y;
        lightPosition[2] += z;
    };

    public void changeColor(String type) {
        switch (type) {
            case "teapot":
                teapotColorIndex = ((teapotColorIndex + 1) % colorList.size());
                break;
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
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT, FloatBuffer.wrap(colorList.get(teapotColorIndex)));
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
}
