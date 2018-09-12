package module;

public class Edge3D {
    private final Point3D a, b;
    private Point3D middle;

    public Point3D getA() {
        return a;
    }

    public Edge3D(Point3D a, Point3D b) {
        this.a = a;
        this.b = b;
        middle = null;
    }

    public Point3D getMiddlePoint() {
        if (middle == null)
            middle = new Point3D (
                    a.getX() + (b.getX() - a.getX()) / 2,
                    a.getY() + (b.getY() - a.getY()) / 2,
                    a.getZ() + (b.getZ() - a.getZ()) / 2
            );

        return middle;
    }
}
