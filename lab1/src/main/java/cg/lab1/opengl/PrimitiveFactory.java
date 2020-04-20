package cg.lab1.opengl;

import java.util.HashMap;
import java.util.Map;

import static cg.lab1.opengl.Primitives.GL_POINTS;

public class PrimitiveFactory {

    private final Map<Primitives, Primitive> primitives;
    private Primitives curPrimitive;

    public PrimitiveFactory() {
        primitives = new HashMap<>();
        curPrimitive = GL_POINTS;
    }

    public Primitive getCurPrimitive() {
        return primitives.computeIfAbsent(curPrimitive, PrimitiveFactory::createPrimitive);
    }

    public Primitive getNextPrimitive() {
        curPrimitive = curPrimitive.next();
        return getCurPrimitive();
    }

    public Primitive getPrevPrimitive() {
        curPrimitive = curPrimitive.prev();
        return getCurPrimitive();
    }

    private static Primitive createPrimitive(Primitives key) {
        Primitive primitive;
        switch (key) {
            case GL_POINTS:
                primitive = new Points();
                break;
            case GL_LINES:
                primitive = new Lines();
                break;
            case GL_LINE_STRIP:
                primitive = new LineStrip();
                break;
            case GL_LINE_LOOP:
                primitive = new LineLoop();
                break;
            case GL_TRIANGLES:
                primitive = new Triangles();
                break;
            case GL_TRIANGLE_STRIP:
                primitive = new TriangleStrip();
                break;
            case GL_TRIANGLE_FAN:
                primitive = new TriangleFan();
                break;
            case GL_QUADS:
                primitive = new Quads();
                break;
            case GL_QUAD_STRIP:
                primitive = new QuadStrip();
                break;
            case GL_POLYGON:
                primitive = new Polygon();
                break;
            default:
                throw new IllegalStateException("Неизвестный тип примитива");
        }
        return primitive;
    }
}
