package computergraphics.datastructures;

/**
 * Created by Eric on 04.01.2016.
 * use this for nodes to be appear in raytracing-rendering
 */
public interface IRaytraceable {
    IntersectionResult findIntersection(Ray3D ray);
}
