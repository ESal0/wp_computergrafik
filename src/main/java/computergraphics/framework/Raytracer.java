package computergraphics.framework;

import computergraphics.datastructures.IRaytraceable;
import computergraphics.datastructures.IntersectionResult;
import computergraphics.datastructures.Ray3D;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.Node;
import computergraphics.scenegraph.RootNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Creates a raytraced image of the current scene.
 */
public class Raytracer {

    /**
     * Reference to the current camera.
     */
    private final Camera camera;
    private final RootNode rootNode;
    private ArrayList<Node> nodes;

    /**
     * Constructor.
     *
     * @param camera   Scene camera.
     * @param rootNode Root node of the scenegraph.
     */
    public Raytracer(Camera camera, RootNode rootNode) {
        this.camera = camera;
        this.rootNode = rootNode;
    }

    /**
     * Creates a raytraced image for the current view with the provided
     * resolution. The opening angle in x-direction is grabbed from the camera,
     * the opening angle in y-direction is computed accordingly.
     *
     * @param resolutionX X-Resolution of the created image.
     * @param resolutionY Y-Resolution of the created image.
     */
    public Image render(int resolutionX, int resolutionY) {
        BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_RGB);

        Vector3 viewDirection = camera.getRef().subtract(camera.getEye()).getNormalized();
        Vector3 xDirection = viewDirection.cross(camera.getUp()).getNormalized();
        Vector3 yDirection = viewDirection.cross(xDirection).getNormalized();
        double openingAngleYScale = Math.sin(camera.getOpeningAngle() * Math.PI / 180.0);
        double openingAngleXScale = openingAngleYScale * (double) resolutionX / (double) resolutionY;

        nodes = collectNodes();

        for (int i = 0; i < resolutionX; i++) {
            double alpha = (double) i / (double) (resolutionX + 1) - 0.5;
            for (int j = 0; j < resolutionY; j++) {
                double beta = (double) j / (double) (resolutionY + 1) - 0.5;
                Vector3 rayDirection = viewDirection.add(xDirection.multiply(alpha * openingAngleXScale))
                        .add(yDirection.multiply(beta * openingAngleYScale)).getNormalized();
                Ray3D ray = new Ray3D(camera.getEye(), rayDirection);

                Vector3 color = trace(ray, 0);

                // Adjust color boundaries
                for (int index = 0; index < 3; index++) {
                    color.set(index, Math.max(0, Math.min(1, color.get(index))));
                }

                image.setRGB(i, j,
                        new Color((int) (255 * color.get(0)), (int) (255 * color.get(1)), (int) (255 * color.get(2))).getRGB());
            }
        }

        return image;
    }

    /**
     * Compute a color from tracing the ray into the scene.
     *
     * @param ray       Ray which needs to be traced.
     * @param recursion Current recursion depth. Initial recursion depth of the rays
     *                  through the image plane is 0. This parameter is used to abort the
     *                  recursion.
     * @return Color in RGB. All values are in [0,1];
     */
    private Vector3 trace(Ray3D ray, int recursion) {
        // Your task
        Vector3 result = new Vector3(0, 0, 0);
        IntersectionResult closestIntersection = new IntersectionResult();
        closestIntersection.point = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        //get the "closest" intersection of the camera
        for (Node n : nodes) {
            if (n instanceof IRaytraceable) {
                IntersectionResult intersection = ((IRaytraceable) n).findIntersection(ray);
                if (intersection != null && intersection.point.getNorm() < closestIntersection.point.getNorm()) {
                    closestIntersection = intersection;
                }
            }
        }

        //does the lighting
        if (closestIntersection.object != null) {
            for (int i = 0; i < rootNode.getNumberOfLightSources(); i++) {
                Vector3 lightVector = (rootNode.getLightSource(i).getPosition().subtract(closestIntersection.point.getNormalized())).getNormalized();
                Vector3 objectColour = closestIntersection.object.getColour();
                Vector3 lightColour = rootNode.getLightSource(i).getColor();

                //shadows
                Ray3D shadowRay = new Ray3D(closestIntersection.point, lightVector.getNormalized());
                if (traceShadows(shadowRay, 0)) {
                    result.add(new Vector3());
                    break;
                }

                //diffuse
                double buffer = lightVector.multiply(closestIntersection.normal);
                if (buffer > 0.0) {
                    Vector3 diffuseColor = objectColour.multiply(buffer);
                    diffuseColor.set(0, diffuseColor.get(0) * lightColour.get(0));
                    diffuseColor.set(1, diffuseColor.get(1) * lightColour.get(1));
                    diffuseColor.set(2, diffuseColor.get(2) * lightColour.get(2));
                    result = result.add(diffuseColor);
                }

                //specular
                double r1 = lightVector.multiply(closestIntersection.normal) * 2;
                Vector3 r2 = closestIntersection.normal.multiply(r1);
                Vector3 r = lightVector.subtract(r2);
                //buffer = r.multiply(ray.getDirection().multiply(-1)); //this should be the formula, but then the "highlighs" appear in the shadows
                buffer = r.multiply(ray.getDirection());
                if (buffer > 0.0) {
                    Vector3 specularColor = new Vector3(1, 1, 1).multiply(Math.pow(buffer, 20.0));
                    specularColor.set(0, specularColor.get(0) * lightColour.get(0));
                    specularColor.set(1, specularColor.get(1) * lightColour.get(1));
                    specularColor.set(2, specularColor.get(2) * lightColour.get(2));
                    result = result.add(specularColor);
                }
            }
        }
        return result;
    }

    //collects -all- nodes from the scenegraph. probably theres a better solution (end-recursive)
    private ArrayList<Node> collectNodes() {
        HashSet<Node> res = new HashSet<>();
        ArrayList<Node> done = new ArrayList<>();
        res.add(rootNode);
        boolean modified = true;

        while (modified) {
            modified = false;
            HashSet<Node> buffer = new HashSet<>();

            for (Node n : res) {
                buffer.addAll(n.getAllChildren());
                done.add(n);
            }
            res.clear();
            res.addAll(buffer);
            if (!res.isEmpty()) {
                modified = true;
            }
        }
        System.out.println("# of nodes: " + done.size());
        return new ArrayList<>(done);
    }

    //TODO: delete or do this before wednesday
    //ignore this, unless good end-recursion is found for collecting traceable nodes
    private Vector3 _trace(Ray3D ray, int recursion, ArrayList<Node> nodes, Node lastNode) {
        if (lastNode.getNumberOfChildren() > 0) {
            //ignore this
            boolean placeholder = true;
        }

        return null;
    }

    private boolean traceShadows(Ray3D ray, int recursion) {
        //get the "closest" intersection on a ray
        for (Node n : nodes) {
            if (n instanceof IRaytraceable) {
                IntersectionResult intersection = ((IRaytraceable) n).findIntersection(ray);
                if (intersection != null) {
                    return true;
                }
            }
        }
        return false;
    }
}