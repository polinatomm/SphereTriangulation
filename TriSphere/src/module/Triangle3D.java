package module;

public class Triangle3D {
    private final Edge3D a, b, c;

    private Triangle3D parent;

    public Triangle3D(Edge3D _a, Edge3D _b, Edge3D _c, Triangle3D _parent) {
        a = _a;
        b = _b;
        c = _c;
        parent = _parent;
    }

    public Edge3D getAEdge() { return a; }
    public Edge3D getBEdge() { return b; }
    public Edge3D getCEdge() { return c; }

    public Triangle3D[] splitTriangleInto4(float r) {
        Point3D tempA = a.getMiddlePoint ();
        Point3D tempB = b.getMiddlePoint ();
        Point3D tempC = c.getMiddlePoint ();
        Point3D middleA = new Point3D (tempA.getX() * r / tempA.length(), tempA.getY() * r / tempA.length(), tempA.getZ() * r / tempA.length());
        Point3D middleB = new Point3D (tempB.getX() * r / tempB.length(), tempB.getY() * r / tempB.length(), tempB.getZ() * r / tempB.length());
        Point3D middleC = new Point3D (tempC.getX() * r / tempC.length(), tempC.getY() * r / tempC.length(), tempC.getZ() * r / tempC.length());

        Triangle3D[] triangles = new Triangle3D[4];
        triangles[0] = new Triangle3D (new Edge3D (a.getA (), middleA), new Edge3D (middleA, middleC), new Edge3D (middleC, a.getA ()), this);
        triangles[1] = new Triangle3D (new Edge3D (middleA, b.getA ()), new Edge3D (b.getA (), middleB), new Edge3D (middleB, middleA), this);
        triangles[2] = new Triangle3D (new Edge3D (middleA, middleB), new Edge3D (middleB, middleC), new Edge3D (middleC, middleA), this);
        triangles[3] = new Triangle3D (new Edge3D (c.getA (), middleC), new Edge3D (middleC, middleB), new Edge3D (middleB, c.getA ()), this);
        return triangles;
    }
    public Triangle3D getParent() {
        return parent;
    }
}
