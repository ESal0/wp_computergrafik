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
    private double resolution;
    private int meshID = 0;
    private double derivativePoint = 0.0;
    private boolean toggleUpdate = false;

    public CurveNode(AbstractCurve curve, int resolution) {
        this.curve = curve;
        this.resolution = 1 / (double) resolution;
    }

    private void init(GL2 gl) {
        meshID = gl.glGenLists(meshID + 1);

        gl.glNewList(meshID, GL2.GL_COMPILE);

        drawCurve(gl);
        //drawTangential(gl);
        gl.glEndList();
    }

    private void drawTangential(GL2 gl) {
        gl.glColor3d(0.0, 0.0, 1.0);
        Vector3 derivative = curve.derivative(derivativePoint);
        Vector3 point = curve.evaluate(derivativePoint);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(point.get(0), point.get(1), point.get(2));
        gl.glVertex3d(derivative.get(0) * 1.1, derivative.get(1) * 1.1, derivative.get(2) * 1.1);
        //gl.glVertex3d(derivative.get(0), derivative.get(1), derivative.get(2));
        gl.glEnd();
    }

    public void drawCurve(GL2 gl) {
        double i = 0;
        gl.glColor3d(0, 0, 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        while (i <= 1) {
            Vector3 v = curve.evaluate(i);
            //System.out.println("vector is: " + v);
            gl.glVertex3d(v.get(0), v.get(1), v.get(2));
            i += resolution;
        }
        gl.glEnd();
    }

    @Override
    public void drawGl(GL2 gl) {
        if (meshID == 0 || toggleUpdate) {
            init(gl);
            toggleUpdate = false;
        }
        gl.glCallList(meshID);
    }

    public double getDerivativePoint() {
        return derivativePoint;
    }

    public void setDerivativePoint(double derivativePoint) {
        if (derivativePoint > 0.999) {
            this.derivativePoint = 1.0;
        } else if (derivativePoint < 0.1) {
            this.derivativePoint = 0.0;
        }
        this.derivativePoint = derivativePoint;
        toggleUpdate = true;
    }
}
