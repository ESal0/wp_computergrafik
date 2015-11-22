package computergraphics.datastructures;

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
    private double alpha = 0.3;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTextureFilename(String filename) {
        // TODO Auto-generated method stub

    }

    public void applyLaplaceFilter() {
        //  ArrayList<Vertex> verticesAfterFilter = vertices;
        HashMap<Integer, Vector3> piMap = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            //Vertex vNew = verticesAfterFilter.get(i);
            Vector3 ci = new Vector3();
            ArrayList<Vertex> neighbors = getAllNeighbors(v);
            for (Vertex neighbor : neighbors) {
                ci = ci.add(neighbor.getPosition());
            }

            ci = ci.multiply(1 / neighbors.size());
            ci = ci.multiply(1 - alpha);
            Vector3 pi = (v.getPosition().multiply(alpha));
            piMap.put(i, pi.add(ci));
            
            /*
            System.out.println("Old pos: " + vNew.getPosition());
            System.out.println("pi: " + pi);
            vNew.getPosition().set(0, pi.get(0));
            vNew.getPosition().set(1, pi.get(1));
            vNew.getPosition().set(2, pi.get(2));
            System.out.println("New pos: " + vNew.getPosition());*/
        }

        for (Map.Entry e : piMap.entrySet()) {
            Vertex vertex = vertices.get((Integer) e.getKey());
            Vector3 vector = (Vector3) e.getValue();
            vertex.getPosition().copy(vector);
        }

        //this.vertices = verticesAfterFilter;
        computeTriangleNormals();
        computeVertexNormals();
        setUpdate(true);
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
