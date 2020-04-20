package cg.lab3.geom;

import static java.lang.Math.*;

public class Triangle {

    //Circumscribed circle
    private final Circle circle;
    //Rotation angle
    private final float angle;

    /**
     * Create triangle
     *
     * @param center center coordinates of circumscribed circle
     * @param radius radius of circumscribed circle
     * @param angle  rotation angle
     */
    public Triangle(Point center, float radius, float angle) {
        circle = new Circle(center, radius);
        this.angle = angle;
    }

    public Circle getCircle() {
        return circle;
    }

    public float getAngle() {
        return angle;
    }

    public Point[] getPoints() {
        Point center = circle.getCenter();
        float x0 = center.getX();
        float y0 = center.getY();
        float r = circle.getR();
        Point p0 = new Point(x0 + (float) (r * cos(angle)), y0 + (float) (r * sin(angle)));
        Point p1 = new Point((float) ((3 * x0 - p0.getX()) / 2 + (p0.getY() - y0) * sqrt(3.0d) / 2),
                (float) (-(p0.getX() - x0) * sqrt(3.0d) / 2 + (3* y0 - p0.getY()) / 2));
        Point p2 = new Point((float) ((3 * x0 - p0.getX()) / 2 - (p0.getY() - y0) * sqrt(3.0d) / 2),
                (float) ((p0.getX() - x0) * sqrt(3.0d) / 2 + (3* y0 - p0.getY()) / 2));
        return new Point[]{p0, p1, p2};
    }

    public Triangle reshape(float scale, float angle) {
        return new Triangle(circle.getCenter(), circle.getR() * scale, this.angle + angle);
    }
}
