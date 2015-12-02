package computergraphics.math;

import computergraphics.datastructures.MarchingCube;

import java.util.ArrayList;

/**
 * Created by Eric on 30.11.2015.
 * da
 */
public class ImplicitFunction {

    private ArrayList<ArrayList<Vector3>> area = new ArrayList<>();
    private ArrayList<ArrayList<Double>> values = new ArrayList<>();

    private ArrayList<MarchingCube> marchingCubes;

    public ImplicitFunction() {
        marchingCubes = new ArrayList<>();
        generateArea();
    }

    private void generateArea() {
        Vector3 nextStart = new Vector3(-2.0, -2.0, -2.0);

        for (int z = 0; z < 25; z++) {
            for (int y = 0; y < 25; y++) {
                for (int x = 0; x < 25; x++) {
                    marchingCubes.add(new MarchingCube(nextStart));
                    nextStart = nextStart.add(new Vector3(0.16, 0, 0));
                }
                nextStart = nextStart.add(new Vector3(-4.0, 0.16, 0));
            }
            nextStart = nextStart.add(new Vector3(0, -4.0, 0.16));
        }
    }

    public void sphere(double radius) {
        for (MarchingCube m : marchingCubes) {
            for (Vector3 v : m.getPoints()) {
                double value = Math.pow(v.get(0), 2) + Math.pow(v.get(1), 2) + Math.pow(v.get(2), 2) - Math.pow(radius, 2);
                m.addValue(value);
            }
        }
    }

    public void torus(double innerRadius, double outerRadius) {
        for (MarchingCube m : marchingCubes) {
            for (Vector3 v : m.getPoints()) {
                double value = Math.pow(Math.pow(v.get(0), 2) + Math.pow(v.get(1), 2) + Math.pow(v.get(2), 2)
                        + Math.pow(outerRadius, 2) - Math.pow(innerRadius, 2), 2)
                        - 4 * Math.pow(outerRadius, 2) * (Math.pow(v.get(0), 2) + Math.pow(v.get(1), 2));
                m.addValue(value);
            }
        }
    }

    public void superQuadratic(double expansionX, double expansionY, double expansionZ, double e1, double e2) {
        for (MarchingCube m : marchingCubes) {
            for (Vector3 v : m.getPoints()) {
                double value = Math.pow(Math.pow(Math.pow(v.get(0) / expansionX, 2 / e2)
                        + Math.pow(v.get(1) / expansionY, 2 / e2), e2 / e1)
                        + Math.pow(v.get(2) / expansionZ, 2 / e1), e1 / 2);
                m.addValue(value);
            }
        }
    }

    public ArrayList<MarchingCube> getMarchingCubes() {
        return marchingCubes;
    }
}
