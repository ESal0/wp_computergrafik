package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class CylinderNode extends Node {
	
	private double radTop = 1;
	private double radBottom = 1;
	private double height = 1;
	private int resolution = 1;
	
	public CylinderNode(double radBottom, double radTop, double height, int resolution){
		this.radBottom = radBottom;
		this.radTop = radTop;
		this.height = height;
		this.resolution = resolution;
	}
	
	@Override
	public void drawGl(GL2 gl) {
		gl.glColor3d(0.6, 0.3, 0.2);
		GLU glu = new GLU();
		GLUquadric cylinder = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(cylinder, GLU.GLU_FILL);
		glu.gluQuadricNormals(cylinder, GLU.GLU_SMOOTH);
		glu.gluQuadricOrientation(cylinder, GLU.GLU_OUTSIDE);
		glu.gluCylinder(cylinder, radBottom, radTop, height, resolution, resolution);
	}

}
