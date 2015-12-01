package computergraphics.scenegraph;

/*
 * @Author: Eric Salomon, Christian Rambow
 */

import com.jogamp.opengl.GL2;
import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.ObjIO;
import computergraphics.datastructures.Vertex;
import computergraphics.math.ImplicitFunction;
import computergraphics.math.Vector3;

import java.util.List;

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
        triangleMesh.computeTriangleNormals();
        triangleMesh.computeVertexNormals();
        triangleMesh.calculateCurvature();
    }

    public HalfEdgeTriangleMeshNode(List<Vector3> points, List<Double> values) {
        triangleMesh = new HalfEdgeTriangleMesh();
        triangleMesh.createTriangles(points, values);

       /* triangleMesh.setOppositeHalfEdges();
        triangleMesh.computeTriangleNormals();
        triangleMesh.computeVertexNormals();*/

    }

    public HalfEdgeTriangleMeshNode(ImplicitFunction function) {
        triangleMesh = new HalfEdgeTriangleMesh();
        triangleMesh.createMeshFromImplicitFunction(function);

    }

    private void init(GL2 gl) {
        // starting the DisplayList with index = index+1
        meshID = gl.glGenLists(meshID + 1);
        gl.glNewList(meshID, GL2.GL_COMPILE);

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

            // Counter for limiting reading 3 halfEdges
            int j = 0;

            while (j < 3) {
                // Get the vertex of the halfEdge
                Vertex vertex = halfEdge.getStartVertex();

                gl.glColor3d(vertex.getColor().get(0), vertex.getColor().get(1), vertex.getColor().get(2));

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
        if (triangleMesh.getUpdate() || meshID == 0) {
            init(gl);
            triangleMesh.setUpdate(false);
        }

        // calling (drawing) the DisplayList
        gl.glCallList(meshID);
    }

    public void applyFilter() {
        triangleMesh.applyLaplaceFilter();
    }

    public void calculateCurvature() {
        triangleMesh.calculateCurvature();
    }
}
