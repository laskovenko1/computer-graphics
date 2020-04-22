package cg.lab4.model;

import java.util.Arrays;
import java.util.List;

public class ControlPoint {

    private Point point;
    private float weight = 1.0f;

    public Point getPoint() {
        return point;
    }

    public float getWeight() {
        return weight;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public List<Float> to4DList() {
        return Arrays.asList(point.getX(), point.getY(), 0.0f, weight);
    }
}
