package computergraphics.datastructures;

import computergraphics.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Eric on 30.11.2015.
 * ds
 */
public class MarchingCube {
    //private HashMap<Vector3, Double> points;

    private ArrayList<Vector3> points;
    private ArrayList<Double> values;

    public MarchingCube(Vector3 start) {
        points = new ArrayList<>();
        values = new ArrayList<>();

        Vector3 p1 = new Vector3(start);
        Vector3 p2 = new Vector3(start).add(new Vector3(0.16, 0, 0));
        Vector3 p3 = new Vector3(start).add(new Vector3(0.16, 0.16, 0));
        Vector3 p4 = new Vector3(start).add(new Vector3(0, 0.16, 0));
        Vector3 p5 = new Vector3(start).add(new Vector3(0, 0, 0.16));
        Vector3 p6 = new Vector3(start).add(new Vector3(0.16, 0, 0.16));
        Vector3 p7 = new Vector3(start).add(new Vector3(0.16, 0.16, 0.16));
        Vector3 p8 = new Vector3(start).add(new Vector3(0, 0.16, 0.16));

        this.points.add(p1);
        this.points.add(p2);
        this.points.add(p3);
        this.points.add(p4);
        this.points.add(p5);
        this.points.add(p6);
        this.points.add(p7);
        this.points.add(p8);

        /*points.put(p1,Double.MAX_VALUE);
        points.put(p2,Double.MAX_VALUE);
        points.put(p3,Double.MAX_VALUE);
        points.put(p4,Double.MAX_VALUE);
        points.put(p5,Double.MAX_VALUE);
        points.put(p6,Double.MAX_VALUE);
        points.put(p7,Double.MAX_VALUE);
        points.put(p8,Double.MAX_VALUE);*/
    }

    public ArrayList<Vector3> getPoints() {
        return points;
    }

    public double getValueAt(int index) {
        return values.get(index);
    }

    /*public Set<Vector3> getKeys(){
        return points.keySet();
    }*/

    public void addPoint(Vector3 vector) {
        if (points.size() > 8) {
            points.add(vector);
        } else {
            System.out.println("Error: tried to add point on full cube");
        }
    }

    public void addValue(Double value) {
        values.add(value);
    }

    public ArrayList<Double> getValues() {
        return values;
    }
}
