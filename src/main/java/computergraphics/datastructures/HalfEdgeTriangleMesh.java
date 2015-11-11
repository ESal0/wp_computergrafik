package computergraphics.datastructures;

import java.util.ArrayList;

import computergraphics.math.Vector3;

/*
 * @Author: Eric Salomon, Christian Rambow
 * basic triangle mesh for A2 WP Computergraphic
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh {

	private ArrayList<HalfEdge> halfEdges = new ArrayList<>();
	private ArrayList<Vertex> vertices = new ArrayList<>();
	private ArrayList<TriangleFacet> facettes = new ArrayList<>();

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
		// set one (the first) halfEdge in the facette
		triangle.setHalfEdge(halfEdgeList.get(0));

		// add the triangle to the list
		facettes.add(triangle);
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
			// Get all triangles from the facette
			Vertex v1 = f.getHalfEdge().getStartVertex();
			Vertex v2 = f.getHalfEdge().getNext().getStartVertex();
			Vertex v3 = f.getHalfEdge().getNext().getNext().getStartVertex();

			// substract v1-v2
			Vector3 leftSide = v1.getNormal().subtract(v2.getNormal());
			// substract v1-v3
			Vector3 rightSide = v1.getNormal().subtract(v3.getNormal());

			// rightSide x leftSide
			f.setNormal(leftSide.cross(rightSide));
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
