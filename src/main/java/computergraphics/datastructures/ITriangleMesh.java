/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.datastructures;

/**
 * This interface describes the valid operations for a triangle mesh structure.
 * 
 * @author Philipp Jenke
 */
public interface ITriangleMesh {

  /**
   * Add a new triangle to the mesh with the vertex indices a, b, c. The index
   * of the first vertex is 0.
   *
   * @param vertexIndex1
   *          Container object to represent a triangle.
   */
  void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3);

  /**
   * Add a new vertex to the vertex list. The new vertex is appended to the end
   * of the list.
   * 
   * @param v
   *          Vertex to be added.
   * 
   * @return Index of the vertex in the vertex list.
   */
  int addVertex(Vertex v);

  /**
   * Getter.
   * 
   * @return Number of triangles in the mesh.
   */
  int getNumberOfTriangles();

  /**
   * Getter.
   * 
   * @return Number of vertices in the triangle mesh.
   */
  int getNumberOfVertices();

  /**
   * Getter
   * 
   * @param index
   *          Index of the vertex to be accessed.
   * @return Vertex at the given index.
   */
  Vertex getVertex(int index);

  /**
   * Return the facet at the given index.
   * 
   * @param facetIndex
   *          Index of the facet.
   * @return Facet at the index, null if the index is invalid.
   */
  TriangleFacet getFacet(int facetIndex);

  /**
   * Clear mesh - remove all triangles and vertices.
   */
  void clear();

  /**
   * Compute the normals for all triangles (facets) in the mesh.
   */
  void computeTriangleNormals();

  void computeVertexNormals();

  String getTextureFilename();

  void setTextureFilename(String filename);
}
