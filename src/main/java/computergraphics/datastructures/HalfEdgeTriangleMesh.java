package computergraphics.datastructures;

import java.util.ArrayList;

import computergraphics.math.Vector3;

/*
 * @Author: Eric Salomon, Christian Rambow
 * 
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh {

	private ArrayList<HalfEdge> halfEdges = new ArrayList<>();
	private ArrayList<Vertex> vertices = new ArrayList<>();
	private ArrayList<TriangleFacet> facettes = new ArrayList<>();

	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		Vertex v1 = getVertex(vertexIndex1);
		Vertex v2 = getVertex(vertexIndex2);
		Vertex v3 = getVertex(vertexIndex3);

		HalfEdge he1 = new HalfEdge();
		HalfEdge he2 = new HalfEdge();
		HalfEdge he3 = new HalfEdge();
		he1.setStartVertex(v1);
		he2.setStartVertex(v2);
		he3.setStartVertex(v3);
		he1.setNext(he2);
		he2.setNext(he3);
		he3.setNext(he1);
		v1.setHalfEdge(he1);
		v2.setHalfEdge(he2);
		v3.setHalfEdge(he3);

		halfEdges.add(he1);
		halfEdges.add(he2);
		halfEdges.add(he3);
		TriangleFacet triangle = new TriangleFacet();
		triangle.setHalfEdge(he1);
		facettes.add(triangle);
	}

	public void setOppositeHalfEdges() {
		for (HalfEdge e : halfEdges) {
			for (HalfEdge o : halfEdges) {
				if ((e.getOpposite() == null && o.getOpposite() == null)
						&& (e.getNext().getStartVertex() == o.getStartVertex())
						&& o.getNext().getStartVertex() == e.getStartVertex()) {
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
		return facettes.size();
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
		return facettes.get(facetIndex);
	}

	@Override
	public void clear() {
		halfEdges.clear();
		vertices.clear();
		facettes.clear();
	}

	@Override
	public void computeTriangleNormals() {
		for (TriangleFacet f : facettes) {
			Vertex v1 = f.getHalfEdge().getStartVertex();
			Vertex v2 = f.getHalfEdge().getNext().getStartVertex();
			Vertex v3 = f.getHalfEdge().getNext().getNext().getStartVertex();

			Vector3 normal = ((v1.getNormal().subtract(v2.getNormal())).cross(v1.getNormal().subtract(v3.getNormal())))
					.getNormalized();

			f.setNormal(normal);
		}
	}

	@Override
	public void setTextureFilename(String filename) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTextureFilename() {
		// TODO Auto-generated method stub
		return null;
	}

}
