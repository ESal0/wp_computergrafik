package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

public class TranslationNode extends Node{

	private Vector3 translationVector = new Vector3(0,0,0); 
	
	public TranslationNode(Vector3 translationVector){
		this.translationVector.copy(translationVector);
	}
	
	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();		
		
		// Apply translation
		gl.glTranslatef((float)translationVector.get(0), (float)translationVector.get(1), (float)translationVector.get(2));
		
		// Render all ChildNodes
		for(int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}
		
		// Restore original state
		gl.glPopMatrix();
	}

}
