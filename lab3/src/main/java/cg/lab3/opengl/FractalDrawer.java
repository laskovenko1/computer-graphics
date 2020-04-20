package cg.lab3.opengl;


import cg.lab3.geom.Circle;
import cg.lab3.geom.Point;
import cg.lab3.geom.Triangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;

public class FractalDrawer {

    private final Fractal fractal;

    public FractalDrawer() {
        fractal = new Fractal();
    }

    public synchronized Fractal init() {
        Triangle startTriangle = new Triangle(new Point(0, 0), 0.8f, (float) PI / 2);
        fractal.getTriangles().add(startTriangle);
        return fractal;
    }

    public synchronized void step() {
        List<Triangle> triangles = fractal.getTriangles();
        List<Triangle> newTriangles = new ArrayList<>();
        for (Triangle triangle : triangles) {
            newTriangles.addAll(splitTriangle(triangle));
            newTriangles.add(triangle.reshape(0.78f, (float) PI / 3));
        }
        triangles.clear();
        triangles.addAll(newTriangles);
    }

    private static List<Triangle> splitTriangle(Triangle triangle) {
        float angle = triangle.getAngle();
        Circle circle = triangle.getCircle();
        float newR = 2 * circle.getR() / 3;
        Point center = circle.getCenter();
        if (newR < 0.01f)
            return Collections.singletonList(triangle);

        List<Triangle> result = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            Point newCenter = new Point(center.getX() + (float) (newR * cos(angle + i * 4 * PI / 6)),
                    center.getY() + (float) (newR * sin(angle + i * 4 * PI / 6)));
            result.add(new Triangle(newCenter, newR / 3, (float) (angle + i * 4 * PI / 6)));
        }

        return result;
    }
}
