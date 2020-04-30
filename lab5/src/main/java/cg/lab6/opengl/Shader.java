package cg.lab6.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import java.nio.FloatBuffer;

public class Shader implements GLEventListener {

    private ShaderState state;
    private GLArrayDataServer vertices;
    private float curTime = 0.0f;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2ES2 gl = glAutoDrawable.getGL().getGL2ES2();

        state = new ShaderState();
        state.setVerbose(true);

        final ShaderCode vp = ShaderCode.create(gl,
                GL2ES2.GL_VERTEX_SHADER,
                Shader.class,
                null,
                "./",
                "Fire",
                true);
        final ShaderCode fp = ShaderCode.create(gl,
                GL2ES2.GL_FRAGMENT_SHADER,
                Shader.class,
                null,
                "./",
                "Fire",
                true);
        vp.defaultShaderCustomization(gl, true, true);
        fp.defaultShaderCustomization(gl, true, true);

        final ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.add(gl, vp, System.err);
        shaderProgram.add(gl, fp, System.err);

        state.attachShaderProgram(gl, shaderProgram, true);

        vertices = GLArrayDataServer.createGLSL("vertices", 2, GL.GL_FLOAT, true, 4, GL.GL_STATIC_DRAW);
        vertices.putf(-1);
        vertices.putf(1);
        vertices.putf(1);
        vertices.putf(1);
        vertices.putf(-1);
        vertices.putf(-1);
        vertices.putf(1);
        vertices.putf(-1);
        vertices.seal(gl, true);
        state.ownAttribute(vertices, true);

        GLUniformData resolution = new GLUniformData("resolution", 2,
                FloatBuffer.wrap(new float[]{(float) glAutoDrawable.getSurfaceWidth(), (float) glAutoDrawable.getSurfaceHeight()}));
        state.uniform(gl, resolution);
        state.ownUniform(resolution);

        GLUniformData time = new GLUniformData("time", curTime);
        state.uniform(gl, time);
        state.ownUniform(time);

        vertices.enableBuffer(gl, false);
        state.useProgram(gl, false);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        final GL2ES2 gl = glAutoDrawable.getGL().getGL2ES2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        state.useProgram(gl, true);

        curTime += 0.001f;
        int time = state.getUniformLocation(gl, "time");
        gl.glUniform1f(time, curTime);

        vertices.enableBuffer(gl, true);
        gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
        vertices.enableBuffer(gl, false);

        state.useProgram(gl, false);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
