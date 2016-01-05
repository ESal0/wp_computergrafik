package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import computergraphics.datastructures.IRaytraceable;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;

public class FloorNode extends Node implements IRaytraceable {

    double width, height, depth;
    private Vector3 normal;
    //we need the center, as a point, where we know, its in the floor
    private Vector3 center;


    public FloorNode(double width, double height, double depth, Vector3 colour) {
        //super(width, height, depth);
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.normal = new Vector3(0, 1, 0);
        this.center = new Vector3((-width / 2 + width / 2) / 2, (height / 2 + height / 2) / 2, (-depth / 2 + depth / 2) / 2);
        this.type = "floor";
        this.colour = colour;
    }

    public void drawGl(GL2 gl) {
        /*super.drawGl(gl);
        for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
		      getChildNode(childIndex).drawGl(gl);
		}*/

        gl.glColor3d(colour.get(0), colour.get(1), colour.get(2));
        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3d(0, 1, 0);
        gl.glVertex3d(-width / 2.0, height / 2.0, -depth / 2.0);
        gl.glVertex3d(width / 2.0, height / 2.0, -depth / 2.0);
        gl.glVertex3d(width / 2.0, height / 2.0, depth / 2.0);
        gl.glVertex3d(-width / 2.0, height / 2.0, depth / 2.0);

        gl.glEnd();
    }

    //Finds the intersection. formula is taken from 06_globale_beleuchtungsrechnung
    public IntersectionResult findIntersection(Ray3D ray) {
        IntersectionResult result = new IntersectionResult();

        Vector3 ps = ray.getPoint();
        Vector3 vs = ray.getDirection();

        double lambda = (normal.multiply(center) - normal.multiply(ps)) / normal.multiply(vs);
        if (lambda <= 0.0) {
            return null;
        }
        Vector3 intersection = ps.add(vs.multiply(lambda));
        result.normal = this.normal;
        result.object = this;
        result.point = intersection;
        return result;
    }

    public Vector3 getColour() {
        return colour;
    }
}
