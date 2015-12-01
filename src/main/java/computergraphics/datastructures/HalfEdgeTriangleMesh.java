package computergraphics.datastructures;

import computergraphics.file.reader.CGFileReader;
import computergraphics.math.ImplicitFunction;
import computergraphics.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @Author: Eric Salomon, Christian Rambow
 * basic triangle mesh for A2 WP Computergraphic
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh {

    private ArrayList<HalfEdge> halfEdges = new ArrayList<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<TriangleFacet> facets = new ArrayList<>();
    private CGFileReader reader = new CGFileReader();
    private ArrayList<Integer> lookupTable = reader.readCasesLookupTableByRelativePath("\\CasesLookupTables\\clt.txt").getCasesLookupTable();
    private double alpha = 0.5;
    private boolean update = true;

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        //System.out.println("Creating triangle between: " + vertexIndex1 + "," + vertexIndex2 + "," + vertexIndex3);
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

    public void createMeshFromImplicitFunction(ImplicitFunction function) {
        for (MarchingCube m : function.getMarchingCubes()) {
            createTriangles(m.getPoints(), m.getValues());
        }
    }

    public void createTriangles(List<Vector3> points, List<Double> values) {
        double iso = 5.0;
        //int cases[] = {0, 0, 0, 0, 0, 0, 0, 0};
        int caseIndex = 0;
        int bit = 1;

        for (Double value : values) {
            if (value > iso) {
                //cases[i] = 1;
                caseIndex += bit;
            }
            bit *= 2;
        }

        //System.out.println("CaseIndex: " + caseIndex);
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = caseIndex * 15; i <= (caseIndex + 1) * 15 - 1; i++) {
            indices.add(lookupTable.get(i));
        }

        for (Integer index : indices) {
            Vector3 p1 = null;
            Vector3 p2 = null;
            switch (index) {
                case 0:
                    p1 = points.get(0);
                    p2 = points.get(1);
                    break;
                case 1:
                    p1 = points.get(1);
                    p2 = points.get(2);
                    break;
                case 2:
                    p1 = points.get(2);
                    p2 = points.get(3);
                    break;
                case 3:
                    p1 = points.get(0);
                    p2 = points.get(3);
                    break;
                case 4:
                    p1 = points.get(4);
                    p2 = points.get(5);
                    break;
                case 5:
                    p1 = points.get(5);
                    p2 = points.get(6);
                    break;
                case 6:
                    p1 = points.get(6);
                    p2 = points.get(7);
                    break;
                case 7:
                    p1 = points.get(4);
                    p2 = points.get(7);
                    break;
                case 8:
                    p1 = points.get(0);
                    p2 = points.get(4);
                    break;
                case 9:
                    p1 = points.get(1);
                    p2 = points.get(5);
                    break;
                case 10:
                    p1 = points.get(3);
                    p2 = points.get(7);
                    break;
                case 11:
                    p1 = points.get(6);
                    p2 = points.get(2);
                    break;
                default:
                    break;
            }
            if (p1 != null && p2 != null) {
                vertices.add(new Vertex(p1.add(p2).multiply(0.5)));
            }
        }

        for (int f = 0; f < vertices.size(); f += 3) {
            addTriangle(f, f + 1, f + 2);
        }
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
            //Starting Half-Edge
            HalfEdge current = v.getHalfEdge();
            do {
                //adding all normals of the adjacent triangles
                normal = normal.add(current.getFacet().getNormal());

                //get the "next" outgoing halfedge of the vertex
                current = current.getOpposite().getNext();
            } while (current != v.getHalfEdge());

            //set the normal (normalized)
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
        //Map with key = index of vertex, value = new vector
        HashMap<Integer, Vector3> map = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            Vector3 ci = new Vector3();

            ArrayList<Vertex> neighbors = getAllNeighbors(v);
            for (Vertex neighbor : neighbors) {
                //Adding the position of all neighbours
                ci = ci.add(neighbor.getPosition());
            }


            ci = ci.multiply(1.0 / neighbors.size());
            ci = ci.multiply(1 - alpha);
            Vector3 pi = (v.getPosition().multiply(alpha)).add(ci);
            map.put(i, pi);
        }

        for (Map.Entry e : map.entrySet()) {
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

                double part1 = v.getNormal().multiply(triangle.getNormal());
                double part2 = 1;//v.getPosition().getNorm() * triangle.getNormal().getNorm();

                buffer += Math.acos(part1 / part2);
            }

            double gamma = (1.0 / adjacentTriangles.size()) * buffer;
            double curvature = gamma / totalAreaOfFacets;

            if (curvature < minCurvature) {
                minCurvature = curvature;
            } else if (curvature > maxCurvature) {
                maxCurvature = curvature;
            }
            v.setCurvature(curvature);
        }

        for (Vertex v : vertices) {
            double factor = (v.getCurvature() - minCurvature) / (maxCurvature - minCurvature);
            Vector3 color = new Vector3(0, 1 * factor, 0);
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
