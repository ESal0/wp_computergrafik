package computergraphics.datastructures;

import computergraphics.math.Vector3;

/**
 * Created by Eric on 04.01.2016.
 * use this for nodes to be appear in raytracing-rendering
 */
public interface IRaytraceable {
    double getReflectionFactor();
    IntersectionResult findIntersection(Ray3D ray);
}
