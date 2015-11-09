package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Vector3;

public class HelicopterNode extends Node {

	private double rotatorAngle = 0;

	@Override
	public void drawGl(GL2 gl) {
		// Creating the nodes

		// Sphere for the Cockpit
		SphereNode cockpit = new SphereNode(0.15, 20);

		// Cylinder for the body of the copter
		CylinderNode copterMiddle = new CylinderNode(0.15, 0.13, 0.3, 20);

		// All "feet" holding the skids
		RotationNode copterFeetRotation = new RotationNode(90, new Vector3(1, 0, 0));
		TranslationNode copterFeetLeftFrontTranslation = new TranslationNode(new Vector3(0.05, 0, 0.12));
		CylinderNode copterFeetLeftFront = new CylinderNode(0.01, 0.01, 0.05, 10);
		TranslationNode copterFeetLeftBackTranslation = new TranslationNode(new Vector3(0.05, 0.2, 0.12));
		CylinderNode copterFeetLeftBack = new CylinderNode(0.01, 0.01, 0.05, 10);
		TranslationNode copterFeetRightFrontTranslation = new TranslationNode(new Vector3(-0.05, 0, 0.12));
		CylinderNode copterFeetRightFront = new CylinderNode(0.01, 0.01, 0.05, 10);
		TranslationNode copterFeetRightBackTranslation = new TranslationNode(new Vector3(-0.05, 0.2, 0.12));
		CylinderNode copterFeetRightBack = new CylinderNode(0.01, 0.01, 0.05, 10);

		// the 2 linding skids
		TranslationNode leftLandingSkidTranslation = new TranslationNode(new Vector3(0.05, -0.165, 0.1));
		CuboidNode leftLandingSkid = new CuboidNode(0.02, 0.01, 0.5);
		TranslationNode rightLandingSkidTranslation = new TranslationNode(new Vector3(-0.05, -0.165, 0.1));
		CuboidNode rightLandingSkid = new CuboidNode(0.02, 0.01, 0.5);

		// The back/closing of the copter
		TranslationNode copterBackClosingTranslation = new TranslationNode(new Vector3(0, 0, 0.3));
		SphereNode copterBackClosing = new SphereNode(0.13, 20);

		// the mast on top of the copter, axis for the rotor
		TranslationNode rotorMastTranslation = new TranslationNode(new Vector3(0, 0.16, 0.15));
		RotationNode rotorMastRotation = new RotationNode(90, new Vector3(1, 0, 0));
		CylinderNode rotorMast = new CylinderNode(0.01, 0.01, 0.025, 10);

		// The main-rotor on top of the copter
		RotationNode rotorRotation = new RotationNode(rotatorAngle, new Vector3(0, 1, 0));
		TranslationNode rotorTranslation = new TranslationNode(new Vector3(0, 0.16, 0.15));
		CuboidNode rotor = new CuboidNode(0.03, 0.003, 1);

		// The tailboom at the back of the copter
		CylinderNode tailBoom = new CylinderNode(0.05, 0.03, 0.9, 7);
		TranslationNode tailBoomEndTranslation = new TranslationNode(new Vector3(0, 0, 0.9));

		// the end of the tailboom
		SphereNode tailBoomEnd = new SphereNode(0.03, 10);

		// The mast of the tail-rotor
		TranslationNode tailRotorMastTranslation = new TranslationNode(new Vector3(0, 0, 0.9));
		RotationNode tailRotorMastRotation = new RotationNode(90, new Vector3(0, 1, 0));
		CylinderNode tailRotorMast = new CylinderNode(0.005, 0.005, 0.04, 20);

		// The tail-rotor
		RotationNode tailRotorRotation = new RotationNode(90, new Vector3(0, 0, 1));
		RotationNode tailRotorAnimationRotation = new RotationNode(rotatorAngle, new Vector3(0, 1, 0));
		TranslationNode tailRotorTranslation = new TranslationNode(new Vector3(0.04, 0, 0.9));
		CuboidNode tailRotor = new CuboidNode(0.03, 0.003, 0.2);

		// Building the Graph

		// Adding the cockpit to the middle
		cockpit.addChild(copterMiddle);

		// Adding the closing to the middle
		copterMiddle.addChild(copterBackClosingTranslation);
		copterBackClosingTranslation.addChild(copterBackClosing);
		copterMiddle.addChild(rotorMastTranslation);

		// Back-right foot of the copter
		copterMiddle.addChild(copterFeetRotation);
		copterFeetRotation.addChild(copterFeetRightBackTranslation);
		copterFeetRightBackTranslation.addChild(copterFeetRightBack);

		// Front-right feet of the copter
		copterMiddle.addChild(copterFeetRotation);
		copterFeetRotation.addChild(copterFeetRightFrontTranslation);
		copterFeetRightFrontTranslation.addChild(copterFeetRightFront);

		// Back-left foot of the copter
		copterMiddle.addChild(copterFeetRotation);
		copterFeetRotation.addChild(copterFeetLeftBackTranslation);
		copterFeetLeftBackTranslation.addChild(copterFeetLeftBack);

		// Front-left foot of the copter
		copterMiddle.addChild(copterFeetRotation);
		copterFeetRotation.addChild(copterFeetLeftFrontTranslation);
		copterFeetLeftFrontTranslation.addChild(copterFeetLeftFront);

		// Left skid of the copter
		copterMiddle.addChild(leftLandingSkidTranslation);
		leftLandingSkidTranslation.addChild(leftLandingSkid);

		// Right skid of the copter
		copterMiddle.addChild(rightLandingSkidTranslation);
		rightLandingSkidTranslation.addChild(rightLandingSkid);

		// Top mast of the copter
		rotorMastTranslation.addChild(rotorMastRotation);
		rotorMastRotation.addChild(rotorMast);

		// Main-Rotor of the copter
		rotorMast.addChild(rotorTranslation);
		rotorTranslation.addChild(rotorRotation);
		rotorRotation.addChild(rotor);

		// Tail-boom of the copter
		copterBackClosing.addChild(tailBoom);
		tailBoom.addChild(tailBoomEndTranslation);

		// End/Closing of the tail-boom
		tailBoomEndTranslation.addChild(tailBoomEnd);

		// Adding the rotor-mast to the tail-boom
		tailBoomEnd.addChild(tailRotorMastTranslation);
		tailRotorMastTranslation.addChild(tailRotorMastRotation);
		tailRotorMastRotation.addChild(tailRotorMast);

		// Adding the rear-rotor to the tailboom
		tailRotorMast.addChild(tailRotorTranslation);
		tailRotorTranslation.addChild(tailRotorRotation);
		tailRotorRotation.addChild(tailRotorAnimationRotation);
		tailRotorAnimationRotation.addChild(tailRotor);

		// Drawing the Copter

		cockpit.drawGl(gl);
		copterMiddle.drawGl(gl);
		copterFeetRotation.drawGl(gl);

		rightLandingSkidTranslation.drawGl(gl);
		leftLandingSkidTranslation.drawGl(gl);

		copterBackClosingTranslation.drawGl(gl);
		tailBoom.drawGl(gl);
		tailBoomEndTranslation.drawGl(gl);

		rotorMastTranslation.drawGl(gl);
		rotorTranslation.drawGl(gl);

		tailRotorMastTranslation.drawGl(gl);
		tailRotorTranslation.drawGl(gl);
	}

	// getter for the rotation of the rotors
	public double getRotatorAngle() {
		return rotatorAngle;
	}

	// setter for the rotation of the rotors
	public void setRotatorAngle(double angle) {
		rotatorAngle = angle;
	}

}
