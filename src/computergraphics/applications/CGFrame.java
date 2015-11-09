/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.ShaderNode;
import computergraphics.scenegraph.ShaderNode.ShaderType;
import computergraphics.scenegraph.SingleTriangleNode;
import computergraphics.scenegraph.SphereNode;

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

  /**
   * Constructor.
   */
  public CGFrame(int timerInverval) {
    super(timerInverval);

    // Shader node does the lighting computation
    ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
    getRoot().addChild(shaderNode);
    
    // Scale Node resizes child nodes
    Vector3 scaleVector = new Vector3(2.0, 2.0, 2.0);
    ScaleNode scaleNode = new ScaleNode(scaleVector);
    shaderNode.addChild(scaleNode);

    // Simple triangle
    SingleTriangleNode triangleNode = new SingleTriangleNode();
    scaleNode.addChild(triangleNode);

    // Sphere
    SphereNode sphereNode = new SphereNode(0.25, 20);
    shaderNode.addChild(sphereNode);
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see computergrafik.framework.ComputergrafikFrame#timerTick()
   */
  @Override
  protected void timerTick() {
    System.out.println("Tick");
  }

  public void keyPressed(int keyCode) {
    System.out.println("Key pressed: " + (char) keyCode);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    // The timer ticks every 1000 ms.
    new CGFrame(1000);
  }
}
