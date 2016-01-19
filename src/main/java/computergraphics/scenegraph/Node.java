/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * <p>
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Parent class for all scene graph nodes.
 *
 * @author Philipp Jenke
 *
 */
public abstract class Node {

    protected String type = "node";
    protected Vector3 colour = null;
    /**
     * List of child nodes
     */
    private List<Node> children = new ArrayList<>();

    /**
     * Add a child node.
     */
    public void addChild(Node child) {
        children.add(child);
    }

    /**
     * Return the child at the given index.
     */
    public Node getChildNode(int index) {
        if (index < 0 || index >= getNumberOfChildren()) {
            System.out.println("getChildNode: invalid index.");
            return null;
        }
        return children.get(index);
    }

    /**
     * Return the number of children
     */
    public int getNumberOfChildren() {
        return children.size();
    }

    /**
     * This method is called to draw the node using OpenGL commands. Override in
     * implementing nodes. Do not forget to call the same method for the children.
     */
    public abstract void drawGl(GL2 gl);

    // DEBUGGING
    public IntersectionResult calcIntersection(Node node, Ray3D ray) {
        return null;
    }

    public IntersectionResult findIntersection(Node object, Ray3D ray) {
        return null;
    }

    public List<Node> getAllChildren() {
        return children;
    }

    public String getType() {
        return type;
    }

    public Vector3 getColour() {
        return colour;
    }

    public Vector3 getColour(Vector3 point) {
        return colour;
    }
}
