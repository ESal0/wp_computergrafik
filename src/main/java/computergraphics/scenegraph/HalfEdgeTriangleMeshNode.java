package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.ObjIO;
import computergraphics.math.Vector3;

public class HalfEdgeTriangleMeshNode extends Node {

	private HalfEdgeTriangleMesh triangleMesh;
	private String path;
	private int meshID = 0;

	public HalfEdgeTriangleMeshNode(String path) {
		this.path = path;
		triangleMesh = new HalfEdgeTriangleMesh();
		ObjIO io = new ObjIO();
		io.einlesen(path, triangleMesh);
		triangleMesh.setOppositeHalfEdges();
	}

	private void init(GL2 gl) {
		meshID = gl.glGenLists(1);
		gl.glNewList(1, GL2.GL_COMPILE);
		drawMesh(gl);
		gl.glEndList();
	}

	private void drawMesh(GL2 gl) {
		gl.glBegin(GL2.GL_TRIANGLES);
		for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
			HalfEdge halfEdge = triangleMesh.getFacet(i).getHalfEdge();
			Vector3 v1 = halfEdge.getStartVertex().getPosition();
			Vector3 v2 = halfEdge.getNext().getStartVertex().getPosition();
			Vector3 v3 = halfEdge.getNext().getNext().getStartVertex().getPosition();
			gl.glColor3d(1.0, 1.0, 0.2);
			gl.glVertex3d(v1.get(0), v1.get(1), v1.get(2));
			gl.glVertex3d(v2.get(0), v2.get(1), v2.get(2));
			gl.glVertex3d(v3.get(0), v3.get(1), v3.get(2));
		}
		gl.glEnd();
	}

	@Override
	public void drawGl(GL2 gl) {
		if (meshID == 0) {
			init(gl);
		}
		gl.glCallList(meshID);
	}

}
