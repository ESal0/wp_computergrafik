package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

public class FloorNode extends CuboidNode{
	
	public FloorNode(double width, double height, double depth) {
		super(width, height, depth);
		// TODO Auto-generated constructor stub
	}
	
	public void drawGl(GL2 gl){
		super.drawGl(gl);
		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
		      getChildNode(childIndex).drawGl(gl);
		}
	}
}
