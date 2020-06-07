package cg.lab8.opengl;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
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
import static com.jogamp.opengl.GL2ES1.GL_LIGHT_MODEL_AMBIENT;
import static com.jogamp.opengl.GL2ES1.GL_LIGHT_MODEL_TWO_SIDE;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_POINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Scene implements GLEventListener {
    private static final float ROTATION_STEP = (float) Math.PI / 72;

    private float xRotation = 0;
    private float zRotation = 0;

    private float scale = 1.0f;

    public void increaseScale() {
        scale += 0.1f;
        if (scale >= 2.0f) {
            scale = 2.0f;
        }
    }
    public void decreaseScale() {
        scale -= 0.1f;
        if (scale <= 0.1f) {
            scale = 0.1f;
        }
    }

    private GL2 gl;
    private List<float[]> colorList = new ArrayList<>();
    private int diffuseColorIndex = 0;

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

    List<float[]> horizontalRopeTopVertexes = new ArrayList<>();
    List<float[]> horizontalRopeBottomVertexes = new ArrayList<>();

    List<float[]> verticalRopeTopVertexes = new ArrayList<>();
    List<float[]> verticalRopeBottomVertexes = new ArrayList<>();

    List<float[]> shortHandleTopVertexes = new ArrayList<>();
    List<float[]> shortHandleBottomVertexes = new ArrayList<>();

    List<float[]> verticalHandleTopVertexes = new ArrayList<>();
    List<float[]> verticalHandleBottomVertexes = new ArrayList<>();

    List<float[]> roofTopVertexes = new ArrayList<>();
    List<float[]> roofBottomVertexes = new ArrayList<>();
    List<float[]> roofSideVertexes = new ArrayList<>();


    private TextureData stoneTexData;
    private int[] stoneTex = new int[1];

    private TextureData woodTexData;
    private int[] woodTex = new int[1];

    private TextureData metalTexData;
    private int[] metalTex = new int[1];

    private TextureData ropeTexData;
    private int[] ropeTex = new int[1];

    private TextureData grassTexData;
    private int[] grassTex = new int[1];

    private ShaderState stoneShaderState;
    private int[] vertexArrayObject = new int[1];
    private int[] vertexBufferObject = new int[1];

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        loadTexture("stone.jpg", stoneTexData, stoneTex);
        loadTexture("wood.jpg", woodTexData, woodTex);
        loadTexture("metal.jpg", metalTexData, metalTex);
        loadTexture("rope.jpg", ropeTexData, ropeTex);
        loadTexture("grass.jpg", grassTexData, grassTex);

        createStoneShader();
        initColor();

        gl.glGenVertexArrays(1, IntBuffer.wrap(vertexArrayObject));
        gl.glGenBuffers(1, IntBuffer.wrap(vertexBufferObject));

        generatePolygon(5.0f, 0f, 90, floorVertexes, true);

        generatePolygon(0.5f, -1f, 30, smallConeBottomVertexes, true);
        generatePolygon(0.5f, 0.0f, 30, smallConeTopVertexes, true);
        generatePolygon(0.6f, -1f, 30, bigConeBottomVertexes, true);
        generatePolygon(0.6f, 0f, 30, bigConeTopVertexes, true);

        generatePolygon(0.05f, 0f, 4, verticalBeamBottomVertexes, true);
        generatePolygon(0.05f, 0.6f, 4, verticalBeamTopVertexes, true);

        generatePolygon(0.05f, -0.55f, 4, horizontalBeamBottomVertexes, false);
        generatePolygon(0.05f, 0.55f, 4, horizontalBeamTopVertexes, false);

        generatePolygon(0.02f, -0.55f, 30, longHandleTopVertexes, false);
        generatePolygon(0.02f, 0.7f, 30, longHandleBottomVertexes, false);

        generatePolygon(0.03f, -0.2f, 30, horizontalRopeTopVertexes, false);
        generatePolygon(0.03f, 0.2f, 30, horizontalRopeBottomVertexes, false);

        generatePolygon(0.008f, -1f, 30, verticalRopeBottomVertexes, true);
        generatePolygon(0.008f, 0.3f, 30, verticalRopeTopVertexes, true);

        generatePolygon(0.02f, 0.7f, 30, shortHandleTopVertexes, false);
        generatePolygon(0.02f, 0.8f, 30, shortHandleBottomVertexes, false);

        generatePolygon(0.02f, 0.13f, 30, verticalHandleTopVertexes, true);
        generatePolygon(0.02f, -0.02f, 30, verticalHandleBottomVertexes, true);


        roofTopVertexes.add(new float[]{1.0f, 0.3f, 0.5f});
        roofTopVertexes.add(new float[]{1.0f, 0.3f, -0.5f});

        roofTopVertexes.add(new float[]{0.05f, 1.0f, 0.5f});
        roofTopVertexes.add(new float[]{0.05f, 1.0f, -0.5f});

        roofTopVertexes.add(new float[]{-0.05f, 1.0f, 0.5f});
        roofTopVertexes.add(new float[]{-0.05f, 1.0f, -0.5f});

        roofTopVertexes.add(new float[]{-1.0f, 0.3f, 0.5f});
        roofTopVertexes.add(new float[]{-1.0f, 0.3f, -0.5f});


        roofBottomVertexes.add(new float[]{-1.0f, 0.2f, 0.5f});
        roofBottomVertexes.add(new float[]{-1.0f, 0.2f, -0.5f});

        roofBottomVertexes.add(new float[]{-0.05f, 0.9f, 0.5f});
        roofBottomVertexes.add(new float[]{-0.05f, 0.9f, -0.5f});

        roofBottomVertexes.add(new float[]{0.05f, 0.9f, 0.5f});
        roofBottomVertexes.add(new float[]{0.05f, 0.9f, -0.5f});

        roofBottomVertexes.add(new float[]{1.0f, 0.2f, 0.5f});
        roofBottomVertexes.add(new float[]{1.0f, 0.2f, -0.5f});

        roofSideVertexes.add(new float[]{1.0f, 0.3f, 0.5f});
        roofSideVertexes.add(new float[]{1.0f, 0.2f, 0.5f});
        roofSideVertexes.add(new float[]{1.0f, 0.3f, -0.5f});
        roofSideVertexes.add(new float[]{1.0f, 0.2f, -0.5f});
        roofSideVertexes.add(new float[]{0.05f, 1.0f, -0.5f});
        roofSideVertexes.add(new float[]{0.05f, 0.9f, -0.5f});
        roofSideVertexes.add(new float[]{-0.05f, 1.0f, -0.5f});
        roofSideVertexes.add(new float[]{-0.05f, 0.9f, -0.5f});
        roofSideVertexes.add(new float[]{-1.0f, 0.3f, -0.5f});
        roofSideVertexes.add(new float[]{-1.0f, 0.2f, -0.5f});
        roofSideVertexes.add(new float[]{-1.0f, 0.3f, 0.5f});
        roofSideVertexes.add(new float[]{-1.0f, 0.2f, 0.5f});
        roofSideVertexes.add(new float[]{-0.05f, 1.0f, 0.5f});
        roofSideVertexes.add(new float[]{-0.05f, 0.9f, 0.5f});
        roofSideVertexes.add(new float[]{0.05f, 1.0f, 0.5f});
        roofSideVertexes.add(new float[]{0.05f, 0.9f, 0.5f});
        roofSideVertexes.add(new float[]{1.0f, 0.3f, 0.5f});
        roofSideVertexes.add(new float[]{1.0f, 0.2f, 0.5f});
    }

    private void loadTexture(String file, TextureData textureData, int[] tex) {
        try {
            final URLConnection urlConnection = IOUtil.getResource(file, Scene.class.getClassLoader());
            final InputStream is = urlConnection.getInputStream();
            final JPEGImage image = JPEGImage.read(is);
            final int format = image.getBytesPerPixel() == 4 ? GL_RGBA : GL_RGB;
            textureData = new TextureData(
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
            is.close();
        } catch (IOException ignored) {
        }
        gl.glGenTextures(1, IntBuffer.wrap(tex));
        gl.glBindTexture(GL_TEXTURE_2D, tex[0]);
        gl.glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                textureData.getWidth() - 1,
                textureData.getHeight() - 1,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                textureData.getBuffer()
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

        gl.glPushMatrix();

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(15f, 1f, 100f, 0f);

        gl.glMatrixMode(GL_MODELVIEW);
        float radius = 10.0f;
        float camX = (float) Math.sin(xRotation) * radius;
        float camZ = (float) Math.cos(zRotation) * radius;
        glu.gluLookAt(scale*camX, scale*1.5f, scale*camZ, 0, 0, 0, 0, 1, 0);
        setLight();

        gl.glPushMatrix();
        gl.glPolygonMode(GL_BACK, GL_POINT);
        gl.glPolygonMode(GL_FRONT, GL_FILL);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);

        //травка
        gl.glBindTexture(GL_TEXTURE_2D, grassTex[0]);
        drawFloor();

        gl.glColor3f(1, 1, 1);

        gl.glEnable(GL_TEXTURE_2D);


        //колодец
        gl.glBindTexture(GL_TEXTURE_2D, stoneTex[0]);
        drawSideWithText(smallConeTopVertexes, smallConeBottomVertexes);
        //веревка, которая висит вниз
        gl.glBindTexture(GL_TEXTURE_2D, ropeTex[0]);
        drawSideWithText(verticalRopeTopVertexes, verticalRopeBottomVertexes);

        gl.glBindTexture(GL_TEXTURE_2D, stoneTex[0]);
        drawSideWithText(bigConeBottomVertexes, bigConeTopVertexes);

        drawSideWithText(bigConeTopVertexes, smallConeTopVertexes);
        drawSideWithText(smallConeBottomVertexes, bigConeBottomVertexes);

        gl.glBindTexture(GL_TEXTURE_2D, woodTex[0]);

        //верхняя часть крыши
        drawRoofSide(roofBottomVertexes);

        //длинная часть ручки
        gl.glBindTexture(GL_TEXTURE_2D, metalTex[0]);
        gl.glTranslatef(0.0f, 0.31f, 0f);
        drawSideWithText(longHandleTopVertexes, longHandleBottomVertexes);

        //веревка на ручке
        gl.glBindTexture(GL_TEXTURE_2D, ropeTex[0]);
        drawSideWithText(horizontalRopeTopVertexes, horizontalRopeBottomVertexes);

        //вертикальная палка
        gl.glBindTexture(GL_TEXTURE_2D, woodTex[0]);
        gl.glTranslatef(-0.55f, -0.31f, 0f);
        drawSideWithText(verticalBeamTopVertexes, verticalBeamBottomVertexes);

        //вертикальная палка
        gl.glTranslatef(1.1f, 0f, 0f);
        drawSideWithText(verticalBeamTopVertexes, verticalBeamBottomVertexes);

        //горизонтальная палка
        gl.glTranslatef(-0.55f, 0.55f, 0f);
        drawSideWithText(horizontalBeamTopVertexes, horizontalBeamBottomVertexes);

        gl.glBindTexture(GL_TEXTURE_2D, metalTex[0]);

        //короткая часть ручки
        gl.glTranslatef(0.0f, -0.35f, 0f);
        drawSideWithText(shortHandleTopVertexes, shortHandleBottomVertexes);

        //вертикальная часть ручки палка
        gl.glTranslatef(0.7f, 0f, 0f);
        drawSideWithText(verticalHandleTopVertexes, verticalHandleBottomVertexes);

        gl.glBindTexture(GL_TEXTURE_2D, woodTex[0]);

        gl.glTranslatef(-0.7f, -0.2f, 0f);
        drawRoofSide(roofTopVertexes);
        drawRoofSide(roofSideVertexes);

        gl.glDisable(GL_DEPTH_TEST);
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    private void drawFloor() {
        gl.glColor3f(0.1f, 0.99f, 0.1f);
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = floorVertexes.size() - 1; i >= 0; --i) {
            gl.glVertex3f(floorVertexes.get(i)[0], floorVertexes.get(i)[1], floorVertexes.get(i)[2]);
        }
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    private void setLight() {
        float[] ambient = {1.0f, 1.0f, 1.0f, 0.0f};
        float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] position = {-1.0f, 3.0f, 5.0f, 1.0f};

        float[] mirrorIntensity = {0.5f, 0.5f, 0.5f, 1.0f};

        gl.glClearColor(1.0f, 0.9390196f, 0.9194118f, 1.0f);

        gl.glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);
        gl.glShadeModel(GL_SMOOTH);

        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, FloatBuffer.wrap(ambient));
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, FloatBuffer.wrap(colorList.get(diffuseColorIndex)));
        gl.glLightfv(GL_LIGHT1, GL_SPECULAR, FloatBuffer.wrap(specular));
        gl.glLightfv(GL_LIGHT1, GL_POSITION, FloatBuffer.wrap(position));
        gl.glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, 1.0f);
        gl.glLightf(GL_LIGHT1, GL_LINEAR_ATTENUATION, 0.1f);
        gl.glLightf(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, 0.001f);

    //    gl.glEnable(GL_COLOR_MATERIAL);
        gl.glMaterialfv(GL_FRONT, GL_SHININESS, FloatBuffer.wrap(mirrorIntensity)); //степень отражения
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, FloatBuffer.wrap(new float[]{1.0f, 0.9390196f, 0.9194118f, 1.0f})); //цвет отражения

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT1);
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
            if (y) {
                vertexList.add(new float[]{(float) cos(a) * radius, coordinate, (float) sin(a) * radius});
            } else {
                vertexList.add(new float[]{coordinate, (float) cos(a) * radius, (float) sin(a) * radius});
            }
        }
    }



    private void drawSideWithText(List<float[]> firstArr, List<float[]> secondArr) {
        float x_tex = 0;
        float tex_step = (float) 1 / firstArr.size();
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < firstArr.size(); ++i) {
            gl.glTexCoord2f(x_tex, 0f);
            gl.glVertex3f(firstArr.get(i)[0], firstArr.get(i)[1], firstArr.get(i)[2]);
            gl.glTexCoord2f(x_tex, 1f);
            gl.glVertex3f(secondArr.get(i)[0], secondArr.get(i)[1], secondArr.get(i)[2]);
            x_tex += tex_step;
        }
        gl.glTexCoord2f(x_tex, 0f);
        gl.glVertex3f(firstArr.get(0)[0], firstArr.get(0)[1], firstArr.get(0)[2]);
        gl.glTexCoord2f(x_tex, 1f);
        gl.glVertex3f(secondArr.get(0)[0], secondArr.get(0)[1], secondArr.get(0)[2]);
        gl.glEnd();
    }

    private void drawRoofSide(List<float[]> roofSideArray) {
        float x_tex = 0;
        float tex_step = (float) 1 / roofSideArray.size();
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < roofSideArray.size(); ++i) {
            if (i % 2 == 0) {
                gl.glTexCoord2f(x_tex, 0f);
            } else {
                gl.glTexCoord2f(x_tex, 1f);
            }
            gl.glVertex3f(roofSideArray.get(i)[0], roofSideArray.get(i)[1], roofSideArray.get(i)[2]);
            x_tex += tex_step;
        }
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

    private void initColor() {
        colorList.add(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        colorList.add(new float[]{1.0f, 0.0f, 0.0f, 1.0f});
        colorList.add(new float[]{1.0f, 0.5f, 0.0f, 1.0f});
        colorList.add(new float[]{1.0f, 1.0f, 0.0f, 1.0f});
        colorList.add(new float[]{0.0f, 1.0f, 0.0f, 1.0f});
        colorList.add(new float[]{0.0f, 0.5f, 0.5f, 1.0f});
        colorList.add(new float[]{0.0f, 0.0f, 1.0f, 1.0f});
        colorList.add(new float[]{0.5f, 0.0f, 1.0f, 1.0f});
    }

    public void changeColor() {
        diffuseColorIndex = ((diffuseColorIndex + 1) % colorList.size());
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
