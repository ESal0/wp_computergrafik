package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

public class RotationNode extends Node{

	private double angle = 0.0;
	private Vector3 axis = new Vector3(0,0,0);
	
	public void setAngle(double angle){
		this.angle = angle;
	}
	
	public double getAngle(){
		return angle;
	}
	
	public RotationNode(double angle, Vector3 axis){
		this.angle = angle;
		this.axis.copy(axis);
	}
	
	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
	    gl.glPushMatrix();

	    // Apply rotation
	    gl.glRotated(angle, axis.get(0), axis.get(1), axis.get(2));

	    // Draw all children
	    for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
	      getChildNode(childIndex).drawGl(gl);
	    }

	    // Restore original state
	    gl.glPopMatrix();

	  }
}

