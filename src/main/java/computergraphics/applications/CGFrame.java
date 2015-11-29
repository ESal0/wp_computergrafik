/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * <p>
 * Base framework for "WP Computergrafik".
 */

package computergraphics.applications;

import com.jogamp.newt.event.KeyEvent;
import computergraphics.file.reader.CGFileReader;
import computergraphics.file.reader.model.CasesLookupTable;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.*;
import computergraphics.scenegraph.ShaderNode.ShaderType;

import java.io.File;

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
    private HalfEdgeTriangleMeshNode triangleMesh;

    /**
     * Constructor.
     */
    public CGFrame(int timerInverval) {
        super(timerInverval);
        excercise3();
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {
        // The timer ticks every 1000 ms.

        //CLT Reader Example
        CGFileReader cgfr = new CGFileReader();
        CasesLookupTable clt = cgfr.readCasesLookupTableByRelativePath("\\CasesLookupTables\\clt.txt");

        new CGFrame(1000);
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
        if (keyCode == KeyEvent.VK_S) {
            System.out.println(true);
            triangleMesh.applyFilter();
            triangleMesh.calculateCurvature();
        }
    }

    private void excercise3() {
        ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
        this.getRoot().addChild(shaderNode);
        triangleMesh = new HalfEdgeTriangleMeshNode("meshes\\cow.obj");
        shaderNode.addChild(triangleMesh);
    }

	/* generates amount of trees and randomly places on Floorwidth*Floorbreadth
     area
	 */

    private void generateTrees(int amount) {
        for (int i = 0; i < amount; i++) {
            TranslationNode translation = new TranslationNode(new Vector3(Math.random() * FLOORWIDTH - FLOORWIDTH / 2,
                    0, Math.random() * FLOORBREADTH - FLOORBREADTH / 2));
            floorNode.addChild(translation);
            translation.addChild(new TreeNode());
        }
    }

    private void excercise2() {
        // Shader node does the lighting computation
        ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
        getRoot().addChild(shaderNode);

        // Adding the floor
        floorNode = new FloorNode(FLOORWIDTH, 0.1, FLOORBREADTH);
        shaderNode.addChild(floorNode);

        // adding the helicopter
        helicopter = new HelicopterNode();
        RotationNode copterRotation = new RotationNode(-90, new Vector3(1, 0, 0));

        // Translation of the helicopter
        helicopterTranslation = new TranslationNode(new Vector3(0, 1.5, 0));

        shaderNode.addChild(helicopterTranslation);
        helicopterTranslation.addChild(copterRotation);
        helicopterTranslation.addChild(helicopter);

        // Generate trees and randomly place them
        generateTrees(25);
    }

    private void excercise1() {
        // Shader node does the lighting computation
        ShaderNode shaderNode = new ShaderNode(ShaderType.PHONG);
        getRoot().addChild(shaderNode);

        TranslationNode translationNode = new TranslationNode(new Vector3(0, 0, 1));
        shaderNode.addChild(translationNode);

        RotationNode rotationNode = new RotationNode(1.5, new Vector3(1, 1, 1));
        translationNode.addChild(rotationNode);

        // Scale node to enhance the following triangle
        ScaleNode scaleNode = new ScaleNode(new Vector3(0.5, 0.5, 0.5));
        rotationNode.addChild(scaleNode);

        // Simple triangle
        SingleTriangleNode triangleNode = new SingleTriangleNode();
        scaleNode.addChild(triangleNode);

        // Sphere
        SphereNode sphereNode = new SphereNode(0.25, 20);
        shaderNode.addChild(sphereNode);
    }
}
