package computergraphics.scenegraph;

/*
 * @Author: Eric Salomon, Christian Rambow
 */
import com.jogamp.opengl.GL2;

import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.ObjIO;
import computergraphics.datastructures.Vertex;

public class HalfEdgeTriangleMeshNode extends Node {

	private HalfEdgeTriangleMesh triangleMesh;
	// GL-intern index of the DisplayList
	private int meshID = 0;

	public HalfEdgeTriangleMeshNode(String path) {
		triangleMesh = new HalfEdgeTriangleMesh();

		// Reading the mesh out of an .OBJ-file
		ObjIO io = new ObjIO();
		io.einlesen(path, triangleMesh);

		// set the opposites for each halfEdge in the mesh
		triangleMesh.setOppositeHalfEdges();
	}

	private void init(GL2 gl) {
		// starting the DisplayList with index = 1
		meshID = gl.glGenLists(1);
		gl.glNewList(1, GL2.GL_COMPILE);

		// drawing the mesh
		drawMesh(gl);
		gl.glEndList();
	}

	private void drawMesh(GL2 gl) {
		// Begin of a triangle
		gl.glBegin(GL2.GL_TRIANGLES);
		for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
			// Get the halfEdge of the facet
			HalfEdge halfEdge = triangleMesh.getFacet(i).getHalfEdge();

			// Color = brown(-ish)
			gl.glColor3d(0.6, 0.4, 0.2);

			// Counter for limiting reading 3 halfEdges
			int j = 0;

			while (j < 3) {
				// Get the vertex of the halfEdge
				Vertex vertex = halfEdge.getStartVertex();

				// setting the normal of the vertex
				gl.glNormal3d(vertex.getNormal().get(0), vertex.getNormal().get(1), vertex.getNormal().get(2));

				// setting the position of the vertex
				gl.glVertex3d(vertex.getPosition().get(0), vertex.getPosition().get(1), vertex.getPosition().get(2));

				// go to the next halfEdge
				halfEdge = halfEdge.getNext();
				j++;
			}
		}
		gl.glEnd();
	}

	@Override
	public void drawGl(GL2 gl) {
		// if the meshID is 0, there is no DisplayList (first call) thanks to
		// Jonas Seegers from the EMIL-Forums
		if (meshID == 0) {
			init(gl);
		}

		// calling (drawing) the DisplayList
		gl.glCallList(meshID);
	}

}
