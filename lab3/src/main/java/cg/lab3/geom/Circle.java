package cg.lab3.geom;

public class Circle {

    private final Point center;
    private final float r;

    public Circle(Point center, float r) {
        this.center = center;
        this.r = r;
    }

    public Point getCenter() {
        return center;
    }

    public float getR() {
        return r;
    }
}
