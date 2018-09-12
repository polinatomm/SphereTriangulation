package module;

import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class SphereModel {
    int iteretion;
    Triangle3D[] triangles;
    Point3D[] spherePoints;

    float R;

    TriangleMesh mesh;

    public SphereModel(float radiuse){
        R = radiuse;
        init();
        toMesh();
    }

    private void init(){
        iteretion = 0;
        float a = (float)((4/Math.sqrt(6))*R);
        float h = (float)(Math.sqrt(2.0/3.0)*a);
        float R1 = (float)(a/Math.sqrt(3));
        float r1 = (float)(a/(2*Math.sqrt(3)));
        triangles = new Triangle3D[4];
        spherePoints = new Point3D[4];
        spherePoints[0] = new Point3D(0, 0, -R);
        spherePoints[1] = new Point3D(0, -R1, h - R);
        spherePoints[2] = new Point3D(a/2, r1, h - R);
        spherePoints[3] = new Point3D(-a/2, r1, h - R);

        triangles[0] = new Triangle3D(new Edge3D(spherePoints[1], spherePoints[2]), new Edge3D( spherePoints[2], spherePoints[3]),new Edge3D(spherePoints[3], spherePoints[1]), null);
        triangles[1] = new Triangle3D(new Edge3D(spherePoints[0], spherePoints[1]), new Edge3D(spherePoints[1], spherePoints[3]), new Edge3D(spherePoints[3], spherePoints[0]), null);
        triangles[2] = new Triangle3D(new Edge3D(spherePoints[0], spherePoints[3]), new Edge3D(spherePoints[3], spherePoints[2]), new Edge3D(spherePoints[2], spherePoints[0]), null);
        triangles[3] = new Triangle3D(new Edge3D(spherePoints[0], spherePoints[2]), new Edge3D(spherePoints[2], spherePoints[1]), new Edge3D(spherePoints[1], spherePoints[0]), null);
    }


    public TriangleMesh getMesh(){
        return mesh;
    }

    private void toMesh() {
        mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(0, 0);

        ArrayList<Point3D> point3Ds = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();

        for (Triangle3D triangle : triangles) {
            int indexA = point3Ds.indexOf(triangle.getAEdge());
            if (indexA < 0) {
                point3Ds.add(triangle.getAEdge().getA());
                indexes.add(point3Ds.size() - 1);
            } else {
                indexes.add(indexA);
            }
            indexes.add(0);
            int indexB = point3Ds.indexOf(triangle.getBEdge());
            if (indexB < 0) {
                point3Ds.add(triangle.getBEdge().getA());
                indexes.add(point3Ds.size() - 1);
            } else {
                indexes.add(indexB);
            }
            indexes.add(0);
            int indexC = point3Ds.indexOf(triangle.getCEdge());
            if (indexC < 0) {
                point3Ds.add(triangle.getCEdge().getA());
                indexes.add(point3Ds.size() - 1);
            } else {
                indexes.add(indexC);
            }
            indexes.add(0);
        }
        float[] points = new float[point3Ds.size() * 3];
        int i = 0;
        for (Point3D point3D : point3Ds) {
            points[i] = point3D.getX();
            points[i + 1] = point3D.getY();
            points[i + 2] = point3D.getZ();
            i += 3;
        }

        int[] faces = new int[indexes.size()];
        i = 0;
        for (Integer index : indexes) {
            faces[i] = index;
            i++;
        }

        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(0,0);
        mesh.getFaces().addAll(faces);

        int lenPoints = mesh.getPointElementSize();
        int lenFaces = mesh.getFaceElementSize();
        float[] tmpPoints = new float[lenPoints];
        int[] tmpFaces = new int[lenFaces];
        mesh.getPoints().toArray(tmpPoints);
        mesh.getFaces().toArray(tmpFaces);
    }

    public int refine() {
        Triangle3D[] tempArray = new Triangle3D[triangles.length * 4];
        int i = 0;
        for (Triangle3D triangle : triangles) {
            Triangle3D[] triangleSplitArray = triangle.splitTriangleInto4(R);
            tempArray[i] = triangleSplitArray[0];
            tempArray[i + 1] = triangleSplitArray[1];
            tempArray[i + 2] = triangleSplitArray[2];
            tempArray[i + 3] = triangleSplitArray[3];
            i += 4;
        }
        triangles = tempArray;
        iteretion++;
        toMesh();
        return triangles.length;
    }
    public int unrefine() {
        Triangle3D[] tempArray = new Triangle3D[triangles.length / 4];
        int i = 0;
        for (int j=0; j<tempArray.length; j++) {
            tempArray[j] = triangles[i].getParent();
            i += 4;
        }
        triangles = tempArray;
        iteretion--;
        toMesh();
        return triangles.length;
    }
    public int getNumberOfRefines() {
        return iteretion;
    }

    public void setNumberOfRefines(int numberOfRefines) {
        for (int i = 0; i < numberOfRefines; i++) {
            refine();
        }
        iteretion = numberOfRefines;
    }
}
