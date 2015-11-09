package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

public class TreeNode extends Node{
	private CylinderNode stem;
	private SphereNode crown;
	private TranslationNode translation;
	private RotationNode rotation;
	
	
	public TreeNode(){
		
	}
	
	@Override
	public void drawGl(GL2 gl) {
		//stem of the tree
		stem = new CylinderNode(0.06, 0.04, 0.5, 20);
		
		//crown of the tree
		crown = new SphereNode(0.25, 20);
		
		//moving the crown on ontop the stem
		translation = new TranslationNode(new Vector3(0,0.5,0));
		
		//rotate the entire tree
		rotation = new RotationNode(-90, new Vector3(1,0,0));
		
		//building the graph
		rotation.addChild(stem);
		addChild(translation);
		translation.addChild(crown);
		
		//draw the parts
		rotation.drawGl(gl);
		translation.drawGl(gl);
	}

}
