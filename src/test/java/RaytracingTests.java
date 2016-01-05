import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.framework.Raytracer;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.FloorNode;
import computergraphics.scenegraph.RootNode;
import computergraphics.scenegraph.SphereNode;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by Eric on 05.01.2016.
 * simple tests for intersecting the sphere and the floor
 */
public class RaytracingTests {

    RootNode rootNode;
    SphereNode sphereNode;
    FloorNode floorNode;
    Raytracer raytracer;
    Ray3D ray1;
    Ray3D ray2;

    @Before
    public void init() {
        sphereNode = new SphereNode(1, 1, new Vector3(0, 0, 0), new Vector3(1, 1, 1));
        floorNode = new FloorNode(1, 1, 1, new Vector3(0, 0, 0));
        ray1 = new Ray3D(new Vector3(5, 5, 5), new Vector3(-1, -1, -1).getNormalized());
        ray2 = new Ray3D(new Vector3(5, 5, 5), new Vector3(99, 99, 99).getNormalized());
    }

    @Test
    public void testSphereTracing() {
        IntersectionResult result = sphereNode.findIntersection(ray1);
        SphereNode resultNode = (SphereNode) result.object;
        double helper = result.point.subtract((resultNode.getCenter())).getNorm();
        assertEquals("Should be hitting sphere, does not", result.object, sphereNode);
        assertEquals("intersection is not on sphere", helper * helper - resultNode.getRadius() * resultNode.getRadius(), 0.0, 0.000000001);

        result = sphereNode.findIntersection(ray2);
        assertNull(result);

    }

    @Test
    public void testFloorNodeTracing() {
        IntersectionResult result = floorNode.findIntersection(ray1);

        assertEquals("Should be hitting floor, does not", result.object, floorNode);
        assertEquals("point is not on floor", result.normal.multiply(result.point) - result.normal.multiply(((FloorNode) result.object).getCenter()), 0.0);

        result = floorNode.findIntersection(ray2);
        assertNull(result);
    }
}
