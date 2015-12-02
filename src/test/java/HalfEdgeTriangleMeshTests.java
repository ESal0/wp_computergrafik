import computergraphics.datastructures.HalfEdge;
import computergraphics.datastructures.HalfEdgeTriangleMesh;
import computergraphics.datastructures.ObjIO;
import computergraphics.math.ImplicitFunction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Eric on 19.11.2015.
 * Tests for HalfEdge triangle meshes
 */
public class HalfEdgeTriangleMeshTests {

    HalfEdgeTriangleMesh mesh;
    ObjIO io;

    @Before
    public void init() {
        io = new ObjIO();
        ImplicitFunction function = new ImplicitFunction();
        function.torus(0.2, 0.5);
        mesh = new HalfEdgeTriangleMesh();
        mesh.createMeshFromImplicitFunction(function);
        //io.einlesen("meshes\\cow.obj", mesh);

        mesh.setOppositeHalfEdges();
        mesh.computeTriangleNormals();
    }

    @Test
    public void checkCorrectOppositeByGettingOppositeOfEdgeTest() {
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            int j = 0;
            HalfEdge edge = mesh.getFacet(i).getHalfEdge();
            while (j < 3) {
                HalfEdge opposite = edge.getOpposite();
                assertEquals("opposite is not equal to edge.getOpposite()", edge, opposite.getOpposite());
                j++;
                edge = edge.getNext();
            }
        }
    }

    @Test
    public void checkClosedTriangles() {
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            assertEquals("triangle not closed", mesh.getFacet(i).getHalfEdge(), mesh.getFacet(i).getHalfEdge().getNext().getNext().getNext());
        }
    }
}