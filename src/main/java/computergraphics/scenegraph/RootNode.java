package computergraphics.scenegraph;

import java.util.ArrayList;
import java.util.List;

/**
 * This node is the root node of the scene graph.
 * 
 * @author Philipp Jenke
 */
public class RootNode extends GroupNode {

  /**
   * List of light sources in the scene.
   */
  private List<LightSource> lights = new ArrayList<>();

  /**
   * Set all lights here
   */
  public RootNode() {
    /*Vector3 position = new Vector3(5, 5, 5);
    Vector3 color = new Vector3(1, 1, 1);
    lights.add(new LightSource(position, color));*/
  }

  public int getNumberOfLightSources() {
    return lights.size();
  }

  public LightSource getLightSource(int index) {
    return lights.get(index);
  }

  public void addLightSource(LightSource lightSource) {
    this.lights.add(lightSource);
  }
}
