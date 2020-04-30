package cg.lab6.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_LIGHT_MODEL_AMBIENT;
import static com.jogamp.opengl.GL2ES1.GL_LIGHT_MODEL_TWO_SIDE;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bolt implements GLEventListener {
    private float lightIntensity = 0.5f;
    private boolean axis = true;
    private int projection = 0;
    private float vertexCount = 8;
    private float yAxisRotation = -327.0f;
    private float xAxisRotation = 327.0f;
    private float scale = 1.0f;
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

        setLight(gl);
//        if(projection == 0) {
//            gl.glMatrixMode(GL_MODELVIEW);
//            gl.glLoadIdentity();
//        } else if(projection == 1) {
//            gl.glViewport(1, 1, 500, 500);
//            gl.glMatrixMode(GL_PROJECTION);
//            gl.glLoadIdentity();
//        }

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glRotatef(yAxisRotation, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(xAxisRotation, 1.0f, 0.0f, 0.0f);
        gl.glScaled(scale, scale, scale);

        gl.glLineWidth(0.5f);
        gl.glPolygonMode(GL_FRONT, GL_FILL);

        if(axis) {
            gl.glBegin(GL_LINES);
            gl.glColor4f(0.0f, 0.5f, 0.5f, 0.3f);
            gl.glVertex3f(-10.0f, 0.0f, 0.0f);
            gl.glVertex3f(10.0f, 0.0f, 0.0f);
            gl.glColor4f(0.5f, 0.0f, 0.5f, 0.3f);
            gl.glVertex3f(0.0f, -10.0f, 0.0f);
            gl.glVertex3f(0.0f,10.0f, 0.0f);
            gl.glColor4f(0.5f, 0.5f, 0.0f, 0.3f);
            gl.glVertex3f(0.0f, 0.0f, -10.0f);
            gl.glVertex3f(0.0f,0.0f, 10.0f);
            gl.glEnd();
        }

        gl.glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
        //шапочка
        gl.glBegin(GL2.GL_POLYGON);
        hatFront.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();
        gl.glBegin(GL2.GL_POLYGON);
        hatBack.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();

        //ножка
        gl.glBegin(GL2.GL_POLYGON);
        legFront.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();
        gl.glBegin(GL2.GL_POLYGON);
        legBack.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();

        //резьба
        gl.glBegin(GL2.GL_POLYGON);
        threadFront.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();
        gl.glBegin(GL2.GL_POLYGON);
        threadBack.forEach((vertex) -> gl.glVertex3f(vertex[0], vertex[1], vertex[2]));
        gl.glEnd();

        // шапочка соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for(int i = 0; i < vertexCount; i++){
            gl.glVertex3f(hatBack.get(i)[0], hatBack.get(i)[1], hatBack.get(i)[2]);
            gl.glVertex3f(hatFront.get(i)[0], hatFront.get(i)[1], hatFront.get(i)[2]);
        }
        gl.glEnd();

        //ножка соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for(int i = 0; i < vertexCount; i++){
            gl.glVertex3f(legBack.get(i)[0], legBack.get(i)[1], legBack.get(i)[2]);
            gl.glVertex3f(legFront.get(i)[0], legFront.get(i)[1], legFront.get(i)[2]);
        }
        gl.glEnd();

        //резьба соединительные линии
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for(int i = 0; i < vertexCount; i++){
            gl.glVertex3f(threadBack.get(i)[0], threadBack.get(i)[1], threadBack.get(i)[2]);
            gl.glVertex3f(threadFront.get(i)[0], threadFront.get(i)[1], threadFront.get(i)[2]);
        }
        gl.glEnd();

        gl.glDisable(GL_DEPTH_TEST);
        gl.glDisable(GL_BLEND);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void setUpRotations(float xAxisRotation, float yAxisRotation) {
        this.xAxisRotation = xAxisRotation;
        this.yAxisRotation = yAxisRotation;
    }

    public void setUpBreaking(float vertexCount) {
        this.vertexCount += vertexCount;
        if(this.vertexCount < 2) {
            this.vertexCount = 2.0f;
        }
        if(this.vertexCount > 20) {
            this.vertexCount = 20.0f;
        }
        generateVertexes();
    }

    public void setUpScale(float scale) {
        this.scale += scale;
    }

    public void setUpAxis() {
        this.axis = !axis;
    }

    private void generateVertexes (){
        generatePolygon(0.5f, 0.5f, hatFront);
        generatePolygon(0.5f, 0.3f, hatBack);
        //generate Leg
        generatePolygon(0.22f, 0.3f, legFront);
        generatePolygon(0.22f, -0.2f, legBack);
        //generate thread
        generatePolygon(0.18f, -0.2f, threadFront);
        generatePolygon(0.18f, -0.8f, threadBack);
    }

    private void generatePolygon(float radius, float z, List<float[]> vertexList) {
       vertexList.clear();
        for (int i = 0; i < vertexCount; i++) {
            float a = (float) i / vertexCount * 3.1415f * 2.0f;
            vertexList.add(new float[]{(float) cos(a) * radius, (float) sin(a) * radius, z});
        }
    }

    public void setUpLight(float lightIntensity) {
        this.lightIntensity += lightIntensity;
        if (this.lightIntensity < 0.0f) {
            this.lightIntensity = 0.0f;
        } else if (this.lightIntensity > 1.0f) {
            this.lightIntensity = 1.0f;
        }
    }

    public void setUpProjection() {
        this.projection +=1;
        this.projection = this.projection % 3;
    }

    private void setLight(GL2 gl) {
        // Устанавливаем параметры источника света
        float[] ambient = {lightIntensity, lightIntensity, lightIntensity, 1.0f};
        float[] diffuse = {lightIntensity,lightIntensity, lightIntensity, 1.0f};
        float[] position = {-100.0f, -60.0f, -150.0f, 0.0f};
        // Определяем цвет фона используемый по умолчанию
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Включаем различные тесты
        gl.glDepthFunc(GL_LEQUAL);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_ONE, GL_SRC_COLOR);
        // Задаем источник света
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(ambient));
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(diffuse));
        gl.glLightfv(GL_LIGHT1, GL_POSITION, FloatBuffer.wrap(position));

        // Разрешаем освещение и включаем источник света
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_LIGHT1);
    }
}
