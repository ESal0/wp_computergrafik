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
                    //System.out.println("NextStart: "+nextStart);
                    nextStart = nextStart.add(new Vector3(0.16, 0, 0));
                }
                nextStart = nextStart.add(new Vector3(-4.0, 0.16, 0));
            }
            nextStart = nextStart.add(new Vector3(0, -4.0, 0.16));
        }
    }

    public ArrayList<MarchingCube> sphere(double radius) {
        for (MarchingCube m : marchingCubes) {
            for (Vector3 v : m.getPoints()) {
                double value = Math.pow(v.get(0), 2) + Math.pow(v.get(1), 2) + Math.pow(v.get(2), 2) - Math.pow(radius, 2);
                m.addValue(value);
            }
        }
        return marchingCubes;
    }

    public ArrayList<MarchingCube> getMarchingCubes() {
        return marchingCubes;
    }
}
