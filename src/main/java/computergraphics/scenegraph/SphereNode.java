/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * <p>
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import computergraphics.datastructures.IRaytraceable;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;

/**
 * Geometry of a simple sphere.
 *
 * @author Philipp Jenke
 */
public class SphereNode extends Node implements IRaytraceable {

    /**
     * Sphere radius.
     */
    private double radius;

    /**
     * Center of the sphere
     */
    private Vector3 center;
    /**
     * Resolution (in one dimension) of the mesh.
     */
    private int resolution;

    /*
     * Factor of how much this object reflects light
     */
    private double reflectionFactor;

    /**
     * Constructors.
     */

    public SphereNode(double radius, int resolution, Vector3 center, Vector3 colour, double reflectionFactor) {
        this.center = center;
        this.radius = radius;
        this.resolution = resolution;
        this.reflectionFactor = reflectionFactor;
        this.colour = colour;
        this.type = "sphere";
    }

    public SphereNode(double radius, int resolution, Vector3 center) {
        this.center = center;
        this.radius = radius;
        this.resolution = resolution;
        this.colour = new Vector3(0.15, 0.50, 0.15);
        this.type = "sphere";
        this.reflectionFactor = 0.0;
    }

    public SphereNode(double radius, int resolution) {
        this.center = new Vector3();
        this.radius = radius;
        this.resolution = resolution;
        this.colour = new Vector3(0.15, 0.50, 0.15);
        this.type = "sphere";
        this.reflectionFactor = 0.0;
    }

    @Override
    public void drawGl(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3d(colour.get(0), colour.get(1), colour.get(2));
        gl.glTranslated(center.get(0), center.get(1), center.get(2));
        GLU glu = new GLU();
        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
        glu.gluQuadricNormals(earth, GLU.GLU_SMOOTH);
        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
        final int slices = resolution;
        final int stacks = resolution;
        glu.gluSphere(earth, radius, slices, stacks);
        gl.glPopMatrix();
    }

    @Override
    public double getReflectionFactor() {
        return this.reflectionFactor;
    }

    //Finds the intersection. formula is taken from 06_globale_beleuchtungsrechnung
    @Override
    public IntersectionResult findIntersection(Ray3D ray) {
        Vector3 ps = ray.getPoint();
        Vector3 vs = ray.getDirection();

        double p = (ps.multiply(2).multiply(vs) - center.multiply(2).multiply(vs)) / vs.multiply(vs);
        double q = (ps.multiply(ps) - ps.multiply(2).multiply(center) + center.multiply(center) - radius * radius) / vs.multiply(vs);

        double l1 = -(p / 2) + Math.sqrt((p * p) / 4 - q);
        double l2 = -(p / 2) - Math.sqrt((p * p) / 4 - q);

        double lambda;

        //One, two or no intersection - take the closest
        if (l1 > 0.00001 && l1 < l2) {
            lambda = l1;
        } else if (l2 > 0.00001) {
            lambda = l2;
        } else {
            return null;
        }

        Vector3 intersection = ps.add(vs.multiply(lambda));
        IntersectionResult result = new IntersectionResult();
        result.point = intersection;
        result.object = this;
        result.normal = new Vector3((result.point.get(0) - center.get(0)) / radius, (result.point.get(1) - center.get(1)) / radius, (result.point.get(2) - center.get(2)) / radius).getNormalized();
        return result;
    }

    public Vector3 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
