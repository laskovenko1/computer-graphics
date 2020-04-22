package cg.lab4.model;

public class Point {

    private final float x;
    private final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isPoint(float x, float y) {
        return Math.abs(this.x - x) < 0.05f && Math.abs(this.y - y) < 0.05f;
    }
}
