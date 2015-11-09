/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.FloorNode;
import computergraphics.scenegraph.HalfEdgeTriangleMeshNode;
import computergraphics.scenegraph.HelicopterNode;
import computergraphics.scenegraph.RotationNode;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.ShaderNode.ShaderType;
import computergraphics.scenegraph.SingleTriangleNode;
import computergraphics.scenegraph.SphereNode;
import computergraphics.scenegraph.TranslationNode;
import computergraphics.scenegraph.TreeNode;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 */
public class CGFrame extends AbstractCGFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 4257130065274995543L;
  private static final double FLOORWIDTH = 5;
  private static final double FLOORBREADTH = 5;
  private FloorNode floorNode;
  private TranslationNode helicopterTranslation;
  private HelicopterNode helicopter;

  /**
   * Constructor.
   */
  public CGFrame(int timerInverval) {
    super(timerInverval);
    excercise3();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see computergrafik.framework.ComputergrafikFrame#timerTick()
   */
  @Override
  protected void timerTick() {
    System.out.println("Tick");
    helicopter.setRotatorAngle(helicopter.getRotatorAngle()+50);
  }

  public void keyPressed(int keyCode) {
    System.out.println("Key pressed: " + (char) keyCode);
  }
  
  private void excercise1(){
    // Shader node does the lighting computation
	ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
	getRoot().addChild(shaderNode);
	
	
	TranslationNode translationNode = new TranslationNode(new Vector3(0,0,1));
	shaderNode.addChild(translationNode);
	
	RotationNode rotationNode = new RotationNode(1.5,new Vector3(1,1,1));
	translationNode.addChild(rotationNode);
	
	// Scale node to enhance the following triangle
	ScaleNode scaleNode = new ScaleNode(new Vector3(0.5,0.5,0.5));
	rotationNode.addChild(scaleNode);
	
	// Simple triangle
	SingleTriangleNode triangleNode = new SingleTriangleNode();
	scaleNode.addChild(triangleNode);
	
	// Sphere
	SphereNode sphereNode = new SphereNode(0.25, 20);
    shaderNode.addChild(sphereNode); 	    
  }
  
  private void excercise2(){
	  // Shader node does the lighting computation
	  ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
	  getRoot().addChild(shaderNode);
	  
	  //Adding the floor
	  floorNode = new FloorNode(FLOORWIDTH,0.1,FLOORBREADTH);	  
	  shaderNode.addChild(floorNode);
	  
	  //adding the helicopter
	  helicopter = new HelicopterNode();
	  RotationNode copterRotation = new RotationNode(-90, new Vector3(1,0,0));
	  
	  //Translation of the helicopter
	  helicopterTranslation = new TranslationNode(new Vector3(0,1.5,0));	  
	  
	  shaderNode.addChild(helicopterTranslation);
	  helicopterTranslation.addChild(copterRotation);
	  helicopterTranslation.addChild(helicopter);
	  
	  //Generate trees and randomly place them
	  generateTrees(25);
  }
  
  private void excercise3(){
	  ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
	  this.getRoot().addChild(shaderNode);
	  floorNode = new FloorNode(FLOORWIDTH, 0.1, FLOORBREADTH);
	  shaderNode.addChild(floorNode);
	  ScaleNode scale = new ScaleNode(new Vector3(2,2,2));
	  TranslationNode translation = new TranslationNode(new Vector3(0,0.3,0));
	  floorNode.addChild(scale);
	  scale.addChild(translation);
	  HalfEdgeTriangleMeshNode triangleMeshNode = new HalfEdgeTriangleMeshNode("meshes\\cow.obj");
	  translation.addChild(triangleMeshNode);
	  generateTrees(15);
  }
  //generates amount of trees and randomly places on Floorwidth*Floorbreadth area
  private void generateTrees(int amount){
	  for(int i = 0; i < amount; i++){
		  TranslationNode translation = new TranslationNode(new Vector3(Math.random()*FLOORWIDTH-FLOORWIDTH/2,0, Math.random()*FLOORBREADTH-FLOORBREADTH/2));
		  floorNode.addChild(translation);
		  translation.addChild(new TreeNode());
	  }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    // The timer ticks every 1000 ms.
    new CGFrame(1000);
  }
}
