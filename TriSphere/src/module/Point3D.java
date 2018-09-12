package module;

public class Point3D {
    private float x;
    private float y;
    private float z;

    public Point3D(float _x, float _y, float _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public float length() {
        return (float) Math.sqrt (x*x + y*y + z*z);
    }

}
