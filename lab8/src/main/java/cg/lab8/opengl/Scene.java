package cg.lab8.opengl;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.GLPixelBuffer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.spi.JPEGImage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Scene implements GLEventListener {
    private static final float ROTATION_STEP = (float) Math.PI / 72;

    private float xRotation = 0;
    private float zRotation = 0;

    private GL2 gl;
    private List<float[]> colorList = new ArrayList<>();
    private int backgroundColorIndex;
    private int mirrorColorIndex;
    private int diffuseColorIndex;
    private int teapotColorIndex;
    private float[] lightPosition = {10.0f, 15.0f, 1.0f, 0.5f};
    private float[] mirrorIntensity = {0.1f, 0.1f, 0.1f, 0.1f};

    List<float[]> floorVertexes = new ArrayList<>();

    List<float[]> smallConeTopVertexes = new ArrayList<>();
    List<float[]> smallConeBottomVertexes = new ArrayList<>();
    List<float[]> bigConeTopVertexes = new ArrayList<>();
    List<float[]> bigConeBottomVertexes = new ArrayList<>();

    List<float[]> verticalBeamTopVertexes = new ArrayList<>();
    List<float[]> verticalBeamBottomVertexes = new ArrayList<>();

    List<float[]> horizontalBeamTopVertexes = new ArrayList<>();
    List<float[]> horizontalBeamBottomVertexes = new ArrayList<>();

    List<float[]> longHandleTopVertexes = new ArrayList<>();
    List<float[]> longHandleBottomVertexes = new ArrayList<>();

    List<float[]> shortHandleTopVertexes = new ArrayList<>();
    List<float[]> shortHandleBottomVertexes = new ArrayList<>();

    List<float[]> verticalHandleTopVertexes = new ArrayList<>();
    List<float[]> verticalHandleBottomVertexes = new ArrayList<>();

    List<float[]> roofTopVertexes = new ArrayList<>();
    List<float[]> leftRoofBottomVertexes = new ArrayList<>();
    List<float[]> rightRoofBottomVertexes = new ArrayList<>();

    private TextureData stoneTexData;
    private int[] stoneTex = new int[1];

    private ShaderState stoneShaderState;
    private int[] vertexArrayObject = new int[1];
    private int[] vertexBufferObject = new int[1];

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        loadStoneTexture();

        createStoneShader();

        gl.glGenVertexArrays(1, IntBuffer.wrap(vertexArrayObject));
        gl.glGenBuffers(1, IntBuffer.wrap(vertexBufferObject));

        backgroundColorIndex = 0;
        mirrorColorIndex = 0;
        diffuseColorIndex = 0;
        teapotColorIndex = 0;

        generatePolygon(5.0f, 0f, 90, floorVertexes, true);

        generatePolygon(0.5f, -1f, 30, smallConeBottomVertexes, true);
        generatePolygon(0.5f, 0.0f, 30, smallConeTopVertexes, true);
        generatePolygon(0.6f, -1f, 30, bigConeBottomVertexes, true);
        generatePolygon(0.6f, 0f, 30, bigConeTopVertexes, true);

        generatePolygon(0.05f, 0f, 4, verticalBeamBottomVertexes, true);
        generatePolygon(0.05f, 0.6f, 4, verticalBeamTopVertexes, true);

        generatePolygon(0.05f, -0.55f, 4, horizontalBeamBottomVertexes, false);
        generatePolygon(0.05f, 0.55f, 4, horizontalBeamTopVertexes, false);

        generatePolygon(0.03f, -0.55f, 30, longHandleTopVertexes, false);
        generatePolygon(0.03f, 0.7f, 30, longHandleBottomVertexes, false);

        generatePolygon(0.02f, 0.7f, 30, shortHandleTopVertexes, false);
        generatePolygon(0.02f, 0.8f, 30, shortHandleBottomVertexes, false);

        generatePolygon(0.02f, 0.13f, 30, verticalHandleTopVertexes, true);
        generatePolygon(0.02f, -0.02f, 30, verticalHandleBottomVertexes, true);

        roofTopVertexes.add(new float[]{0.05f, 1.5f, 0.5f});
        roofTopVertexes.add(new float[]{0.05f, 1.5f, -0.5f});
        roofTopVertexes.add(new float[]{-0.05f, 1.5f, -0.5f});
        roofTopVertexes.add(new float[]{-0.05f, 1.5f, 0.5f});

        leftRoofBottomVertexes.add(new float[]{1.0f, 0.7f, 0.5f});
        leftRoofBottomVertexes.add(new float[]{1.0f, 0.7f, -0.5f});
        leftRoofBottomVertexes.add(new float[]{0.9f, 0.7f, -0.5f});
        leftRoofBottomVertexes.add(new float[]{0.9f, 0.7f, 0.5f});

        rightRoofBottomVertexes.add(new float[]{-0.9f, 0.7f, 0.5f});
        rightRoofBottomVertexes.add(new float[]{-0.9f, 0.7f, -0.5f});
        rightRoofBottomVertexes.add(new float[]{-1.0f, 0.7f, -0.5f});
        rightRoofBottomVertexes.add(new float[]{-1.0f, 0.7f, 0.5f});
    }

    private void loadStoneTexture() {
        try {
            final URLConnection urlConnection = IOUtil.getResource("stone.jpg", this.getClass().getClassLoader());
            InputStream is = urlConnection.getInputStream();
            JPEGImage image = JPEGImage.read(is);
            int format = image.getBytesPerPixel() == 4 ? GL_RGBA : GL_RGB;
            stoneTexData = new TextureData(
                    GLProfile.getGL2ES2(),
                    format,
                    image.getWidth(),
                    image.getHeight(),
                    0,
                    new GLPixelBuffer.GLPixelAttributes(image.getGLFormat(), image.getGLType()),
                    false,
                    false,
                    false,
                    image.getData(),
                    null
            );
        } catch (IOException ignored) {
        }
        gl.glGenTextures(1, IntBuffer.wrap(stoneTex));
        gl.glBindTexture(GL_TEXTURE_2D, stoneTex[0]);
        gl.glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                stoneTexData.getWidth(),
                stoneTexData.getHeight(),
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                stoneTexData.getBuffer()
        );
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void createStoneShader() {
        stoneShaderState = new ShaderState();
        stoneShaderState.setVerbose(true);

        ShaderCode vp = ShaderCode.create(
                gl,
                GL2ES2.GL_VERTEX_SHADER,
                Scene.class,
                null,
                "./",
                "stone",
                true
        );
        vp.defaultShaderCustomization(gl, true, true);
        ShaderCode fp = ShaderCode.create(
                gl,
                GL2ES2.GL_FRAGMENT_SHADER,
                Scene.class,
                null,
                "./",
                "stone",
                true
        );
        fp.defaultShaderCustomization(gl, true, true);

        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.add(gl, vp, System.err);
        shaderProgram.add(gl, fp, System.err);

        stoneShaderState.attachShaderProgram(gl, shaderProgram, true);

        GLUniformData u_texture = new GLUniformData("u_texture", stoneTex[0]);
        stoneShaderState.uniform(gl, u_texture);
        stoneShaderState.ownUniform(u_texture);

        stoneShaderState.useProgram(gl, false);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        final GLUT glut = new GLUT();
        final GLU glu = GLU.createGLU(gl);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //setLight(gl);

        gl.glPushMatrix();

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(15f,1f,100f,0f);

        gl.glMatrixMode(GL_MODELVIEW);
        float radius = 10.0f;
        float camX = (float) Math.sin(xRotation) * radius;
        float camZ = (float) Math.cos(zRotation) * radius;
        glu.gluLookAt(camX, 1.5f, camZ, 0, 0, 0, 0, 1, 0);

        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        drawFloor();

//        gl.glColor3f(1.f, 1.f, 1.f);

        stoneShaderState.useProgram(gl, true);
        //цилиндр
        gl.glBindTexture(GL_TEXTURE_2D, stoneTex[0]);

        gl.glBindVertexArray(vertexArrayObject[0]);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject[0]);
        float[] bigCone = createTexturedSide(bigConeTopVertexes, bigConeBottomVertexes);
        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(bigCone);
        gl.glBufferData(GL_ARRAY_BUFFER, bigCone.length, buffer, GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5, (bigConeTopVertexes.size() + bigConeTopVertexes.size()) * 3);
        gl.glEnableVertexAttribArray(1);

        gl.glBindVertexArray(vertexArrayObject[0]);
        gl.glDrawElements(GL2.GL_QUADS, 30, GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);

        stoneShaderState.useProgram(gl, false);

//        gl.glColor3f(1.f, 1.f, 1.f);
//        drawSide(smallConeTopVertexes, smallConeBottomVertexes);
//        //drawSide(bigConeTopVertexes, bigConeBottomVertexes);
//        drawSide(smallConeTopVertexes, bigConeTopVertexes);
//        drawSide(smallConeBottomVertexes, bigConeBottomVertexes);
//
//        //вертикальная палка
//        gl.glTranslatef(-0.55f, 0f, 0f);
//        drawSide(verticalBeamTopVertexes, verticalBeamBottomVertexes);
//
//        //вертикальная палка
//        gl.glTranslatef(1.1f, 0f, 0f);
//        drawSide(verticalBeamTopVertexes, verticalBeamBottomVertexes);
//
//        //горизонтальная палка
//        gl.glTranslatef(-0.55f, 0.55f, 0f);
//        drawSide(horizontalBeamTopVertexes, horizontalBeamBottomVertexes);
//
//        //длинная часть ручки
//        gl.glTranslatef(0.0f, -0.25f, 0f);
//        drawSide(longHandleTopVertexes, longHandleBottomVertexes);
//
//        //короткая часть ручки
//        gl.glTranslatef(0.0f, -0.1f, 0f);
//        drawSide(shortHandleTopVertexes, shortHandleBottomVertexes);
//
//        //вертикальная часть ручки палка
//        gl.glTranslatef(0.7f, 0f, 0f);
//        drawSide(verticalHandleTopVertexes, verticalHandleBottomVertexes);
//
//        gl.glTranslatef(-0.7f, -0.6f, 0f);
//        drawSide(roofTopVertexes, leftRoofBottomVertexes);
//        drawSide(roofTopVertexes, rightRoofBottomVertexes);

        gl.glPopMatrix();

//        gl.glDisable(GL_DEPTH_TEST);
    }

    private float[] generateCylinderBuff() {
        return new float[] {
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f
        };
    }

    private void drawFloor() {
        gl.glColor3f(0.5f, 0, 0);
        gl.glBegin(GL2.GL_POLYGON);
        for (float[] floorVertex : floorVertexes) {
            gl.glVertex3f(floorVertex[0], floorVertex[1], floorVertex[2]);
        }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    private void setLight(GL2 gl) {
        float[] ambient = {0.1f, 0.1f, 0.1f, 0.0f};
        float[] position = {0.0f, 0.0f, -1.0f, 0.0f};

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);

//        gl.glDepthFunc(GL_LEQUAL);
//        gl.glEnable(GL_DEPTH_TEST);

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(ambient));
//        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(position));
//        gl.glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, 0.0001f);
//        gl.glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 0.0000001f);
//        gl.glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.0000001f);

//        gl.glMaterialfv(GL_FRONT, GL_SHININESS, FloatBuffer.wrap(materialFrontShininess));
//        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, FloatBuffer.wrap(materialFrontDiffuse));
    }
//
//
//    public void setUpRotations(float yAxisRotation) {
//        this.yAxisRotation = yAxisRotation;
//    }

    private void generatePolygon(float radius, float coordinate, int vertexCount, List<float[]> vertexList, boolean y) {
        vertexList.clear();
        for (int i = 0; i < vertexCount; i++) {
            float a = (float) i / vertexCount * 3.1415f * 2.0f;
            if(y) {
                vertexList.add(new float[]{(float) cos(a) * radius, coordinate, (float) sin(a) * radius});
            } else {
                vertexList.add(new float[]{coordinate, (float) cos(a) * radius, (float) sin(a) * radius});
            }
        }
    }

    private float[] createTexturedSide(List<float[]> firstArr, List<float[]> secondArr) {
        List<Float> result = new ArrayList<>();
        float tex_step = (float) 1 / firstArr.size();
        float x_tex = 0;
        for (int i = 0; i < firstArr.size() - 1 ; ++i) {
            result.add(firstArr.get(i)[0]);
            result.add(firstArr.get(i)[1]);
            result.add(firstArr.get(i)[2]);
            result.add(x_tex);
            result.add(0f);
            result.add(secondArr.get(i)[0]);
            result.add(secondArr.get(i)[1]);
            result.add(secondArr.get(i)[2]);
            result.add(x_tex);
            result.add(1f);
            x_tex += tex_step;
        }
        result.add(firstArr.get(0)[0]);
        result.add(firstArr.get(0)[1]);
        result.add(firstArr.get(0)[2]);
        result.add(x_tex);
        result.add(0f);
        result.add(secondArr.get(0)[0]);
        result.add(secondArr.get(0)[1]);
        result.add(secondArr.get(0)[2]);
        result.add(x_tex);
        result.add(1f);

        return ArrayUtils.toPrimitive(result.toArray(new Float[0]));
    }

    private void drawSide(List<float[]> firstArr, List<float[]> secondArr) {
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < firstArr.size() - 1 ; ++i) {
            gl.glVertex3f(firstArr.get(i)[0], firstArr.get(i)[1], firstArr.get(i)[2]);
            gl.glVertex3f(secondArr.get(i)[0], secondArr.get(i)[1], secondArr.get(i)[2]);
        }
        gl.glVertex3f(firstArr.get(0)[0], firstArr.get(0)[1], firstArr.get(0)[2]);
        gl.glVertex3f(secondArr.get(0)[0], secondArr.get(0)[1], secondArr.get(0)[2]);
        gl.glEnd();
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

//    private void drawBeam(List<float[]> topArr, List<float[]> bottomArr) {
//        drawSide(topArr, bottomArr);
////
////        gl.glBegin(GL2.GL_POLYGON);
////        for (int i =  topArr.size() - 1; i >= 0; --i) {
////            gl.glVertex3f(topArr.get(i)[0], topArr.get(i)[1], topArr.get(i)[2]);
////        }
////        gl.glEnd();
////
////        gl.glBegin(GL2.GL_POLYGON);
////        for (float[] floats : bottomArr) {
////            gl.glVertex3f(floats[0], floats[1], floats[2]);
////        }
////        gl.glEnd();
//    }
}
