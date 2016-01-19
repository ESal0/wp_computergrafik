package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import computergraphics.datastructures.IRaytraceable;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;

public class FloorNode extends Node implements IRaytraceable {

    double width, height, depth;

    public Vector3 getNormal() {
        return normal;
    }

    private Vector3 normal;
    //we need the center, as a point, where we know, its in the floor
    private Vector3 center;
    private Vector3 pointInFloor;
    private double reflectionFactor;

    public FloorNode(double width, double height, double depth, double reflectionFactor, Vector3 colour) {
        //super(width, height, depth);
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.reflectionFactor = reflectionFactor;
        this.normal = new Vector3(0, 1, 0);
        this.center = new Vector3((-width / 2 + width / 2) / 2, (height / 2 + height / 2) / 2, (-depth / 2 + depth / 2) / 2);
        this.pointInFloor = center.add(new Vector3(1,0,2));
        System.out.println("center: "+center);
        this.type = "floor";
        this.colour = colour;
    }

    public Vector3 getCenter() {
        return center;
    }

    public void drawGl(GL2 gl) {
        gl.glColor3d(colour.get(0), colour.get(1), colour.get(2));
        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3d(0, 1, 0);
        gl.glVertex3d(-width / 2.0, height / 2.0, -depth / 2.0);
        gl.glVertex3d(width / 2.0, height / 2.0, -depth / 2.0);
        gl.glVertex3d(width / 2.0, height / 2.0, depth / 2.0);
        gl.glVertex3d(-width / 2.0, height / 2.0, depth / 2.0);

        gl.glEnd();
    }

    @Override
    public double getReflectionFactor() {
        return reflectionFactor;
    }

    //Finds the intersection. formula is taken from 06_globale_beleuchtungsrechnung
    public IntersectionResult findIntersection(Ray3D ray) {
        IntersectionResult result = new IntersectionResult();

        Vector3 ps = ray.getPoint();
        Vector3 vs = ray.getDirection();

        double lambda = (normal.multiply(center) - normal.multiply(ps)) / normal.multiply(vs);
        if (lambda <= 0.0000000000001) {
            return null;
        }

        Vector3 intersection = ps.add(vs.multiply(lambda));
        result.normal = this.normal;
        result.object = this;
        result.point = intersection;


        return result;
    }

    public Vector3 getColour(Vector3 intersection) {
        Vector3 tv = this.pointInFloor.cross(getNormal());
        Vector3 tu = tv.cross(getNormal());

        //System.out.println("v" + tv + "  u" + tu);
        double v = tv.multiply(intersection);
        double u = tu.multiply(intersection);

        u = (u < 0.0 ? -u + 1.0 : u);
        v = (v < 0.0 ? -v + 1.0 : v);

        if ((int) u % 2 == (int) v % 2) {
            return new Vector3();
        } else {
            System.out.println("white");
            return new Vector3(1, 1, 1);
        }
    }
}
