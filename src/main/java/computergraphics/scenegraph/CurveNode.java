package computergraphics.scenegraph;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import computergraphics.math.AbstractCurve;
import computergraphics.math.Vector3;

/**
 * Created by Eric on 15.12.2015.
 */
public class CurveNode extends Node {

    private AbstractCurve curve;
    private int resolution;
    private int meshID = 0;

    public CurveNode(AbstractCurve curve, int resolution) {
        this.curve = curve;
        this.resolution = resolution;
    }


    private void init(GL2 gl) {
        meshID = gl.glGenLists(meshID + 1);

        gl.glNewList(meshID, GL2.GL_COMPILE);
        drawCurve(gl);
        gl.glEndList();
    }

    public void drawCurve(GL2 gl) {

        double i = 0;
        double resolution = 1 / (double) this.resolution;
        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glLineWidth(2.0f);
        while (i <= 1) {
            Vector3 v = curve.evaluate(i);
            System.out.println("vector is: " + v);
            gl.glVertex3d(v.get(0), v.get(1), v.get(2));
            i += resolution;
        }
        gl.glEnd();
    }

    @Override
    public void drawGl(GL2 gl) {
        if (meshID == 0) {
            init(gl);
        }
        gl.glCallList(meshID);
    }
}
