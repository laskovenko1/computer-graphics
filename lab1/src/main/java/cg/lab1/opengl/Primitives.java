package cg.lab1.opengl;

public enum Primitives {
    GL_POINTS,
    GL_LINES,
    GL_LINE_STRIP,
    GL_LINE_LOOP,
    GL_TRIANGLES,
    GL_TRIANGLE_STRIP,
    GL_TRIANGLE_FAN,
    GL_QUADS,
    GL_QUAD_STRIP,
    GL_POLYGON;

    private static Primitives[] primitives = values();

    public Primitives next() {
        return primitives[(this.ordinal()+1) % primitives.length];
    }

    public Primitives prev() {
        int i = this.ordinal() - 1;
        while (i < 0) i += primitives.length;
        return primitives[i % primitives.length];
    }
}
