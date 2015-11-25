package computergraphics.datastructures;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import computergraphics.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: Eric Salomon, Christian Rambow
 * basic triangle mesh for A2 WP Computergraphic
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh {

    private ArrayList<HalfEdge> halfEdges = new ArrayList<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<TriangleFacet> facets = new ArrayList<>();
    private double alpha = 0.5;
    private boolean update = true;

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        ArrayList<HalfEdge> halfEdgeList = new ArrayList<>();
        ArrayList<Vertex> vertexList = new ArrayList<>();
        TriangleFacet triangle = new TriangleFacet();

        // fill the vertexList with the vertices
        vertexList.add(getVertex(vertexIndex1));
        vertexList.add(getVertex(vertexIndex2));
        vertexList.add(getVertex(vertexIndex3));

        // fill the halfEdgeList with new halfEdges
        halfEdgeList.add(new HalfEdge());
        halfEdgeList.add(new HalfEdge());
        halfEdgeList.add(new HalfEdge());

        // counter to
        int counter = 0;

        // set the params for all halfEdges
        for (HalfEdge e : halfEdgeList) {
            e.setStartVertex(vertexList.get(counter));
            e.setNext(halfEdgeList.get((counter + 1) % 3));
            vertexList.get(counter).setHalfEdge(e);
            e.setFacet(triangle);
            halfEdges.add(e);
            counter++;
        }
        // set one (the first) halfEdge in the facet
        triangle.setHalfEdge(halfEdgeList.get(0));

        // add the triangle to the list
        facets.add(triangle);
    }

    // TODO: this can be done better ("map the vertices")
    public void setOppositeHalfEdges() {
        for (HalfEdge e : halfEdges) {
            for (HalfEdge o : halfEdges) {
                // if one of the halfEdges has an opposite -> skip
                // then, check if the halfEdges are their opposites
                if ((e.getOpposite() == null && o.getOpposite() == null)
                        && (e.getNext().getStartVertex() == o.getStartVertex())
                        && o.getNext().getStartVertex() == e.getStartVertex()) {
                    // set them as opposites
                    e.setOpposite(o);
                    o.setOpposite(e);
                }
            }
        }
    }

    @Override
    public int addVertex(Vertex v) {
        vertices.add(v);
        return 0;
    }

    @Override
    public int getNumberOfTriangles() {
        return facets.size();
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.size();
    }

    @Override
    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public TriangleFacet getFacet(int facetIndex) {
        return facets.get(facetIndex);
    }

    @Override
    public void clear() {
        halfEdges.clear();
        vertices.clear();
        facets.clear();
    }

    @Override
    public void computeTriangleNormals() {
        for (TriangleFacet f : facets) {
            // Get all triangles from the facette
            Vertex v1 = f.getHalfEdge().getStartVertex();
            Vertex v2 = f.getHalfEdge().getNext().getStartVertex();
            Vertex v3 = f.getHalfEdge().getNext().getNext().getStartVertex();

            // substract v2-v1
            Vector3 leftSide = (v2.getPosition().subtract(v1.getPosition()));
            // substract v3-v1
            Vector3 rightSide = (v3.getPosition().subtract(v1.getPosition()));

            // rightSide x leftSide
            f.setNormal((leftSide.cross(rightSide)).getNormalized());
        }
    }

    @Override
    public void computeVertexNormals() {
        for (Vertex v : vertices) {
            Vector3 normal = new Vector3(0, 0, 0);
            HalfEdge current = v.getHalfEdge();
            do {
                normal = normal.add(current.getFacet().getNormal());
                current = current.getOpposite().getNext();
            } while (current != v.getHalfEdge());
            v.setNormal(normal.getNormalized());
        }
    }

    @Override
    public String getTextureFilename() {
        return null;
    }

    @Override
    public void setTextureFilename(String filename) {

    }

    public void applyLaplaceFilter() {
        HashMap<Integer, Vector3> map = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            Vector3 ci = new Vector3();
            ArrayList<Vertex> neighbors = getAllNeighbors(v);
            for (Vertex neighbor : neighbors) {
                ci = ci.add(neighbor.getPosition());
            }
            ci = ci.multiply(1.0 / neighbors.size());
            ci = ci.multiply(1 - alpha);
            Vector3 pi = (v.getPosition().multiply(alpha)).add(ci);
            map.put(i, pi);
        }

        for(Map.Entry e : map.entrySet()){
            vertices.get((Integer) e.getKey()).getPosition().copy((Vector3) e.getValue());
        }

        computeTriangleNormals();
        computeVertexNormals();
        setUpdate(true);
    }

    public void calculateCurvature() {
        double minCurvature = Double.MAX_VALUE;
        double maxCurvature = 0.0;

        for (Vertex v : vertices) {
            ArrayList<TriangleFacet> adjacentTriangles = getAllAdjacentTriangles(v);
            double totalAreaOfFacets = 0.0;

            double buffer = 0.0;
            for (TriangleFacet triangle : adjacentTriangles) {
                totalAreaOfFacets += triangle.getArea();

                double part1 = v.getPosition().multiply(triangle.getCentroid());
                double part2 = v.getPosition().getNorm() * triangle.getNormal().getNorm();

                buffer += Math.acos(part1 / part2);
            }

            double gamma = (1.0 / adjacentTriangles.size()) * buffer;
            double curvature = gamma / totalAreaOfFacets;

            if (curvature < minCurvature) {
                minCurvature = curvature;
            } else if (curvature > maxCurvature) {
                maxCurvature = curvature;
            }
            //System.out.println(curvature);
            v.setCurvature(curvature);
        }

        for (Vertex v : vertices) {
            double factor = (v.getCurvature() - minCurvature) / (maxCurvature - minCurvature);
            Vector3 color = new Vector3(1, 1*factor, 0);
            //System.out.println("factor: " + factor);
            v.setColor(color);
        }
    }

    private ArrayList<TriangleFacet> getAllAdjacentTriangles(Vertex v) {
        ArrayList<TriangleFacet> triangles = new ArrayList<>();
        HalfEdge current = v.getHalfEdge();
        do {
            triangles.add(current.getFacet());
            current = current.getOpposite().getNext();
        } while (current != v.getHalfEdge());

        return triangles;
    }

    private ArrayList<Vertex> getAllNeighbors(Vertex v) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        HalfEdge current = v.getHalfEdge();
        do {
            neighbors.add(current.getOpposite().getStartVertex());
            current = current.getOpposite().getNext();
        } while (current != v.getHalfEdge());
        return neighbors;
    }

    public boolean getUpdate() {
        return update;
    }

    public void setUpdate(boolean bool) {
        this.update = bool;
        System.out.println("Update: " + update);
    }
}
