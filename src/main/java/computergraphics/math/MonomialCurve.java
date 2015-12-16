package computergraphics.math;

import java.util.ArrayList;

/**
 * Created by Eric on 15.12.2015.
 */
public class MonomialCurve extends AbstractCurve {

    private MonomialCurve() {
    }

    public MonomialCurve(ArrayList<Vector3> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public static MonomialCurve interpolate(ArrayList<Vector3> points) {

        if (points.size() > 3) {
            System.out.println("Too many points, sorry");
            return new MonomialCurve(points);
        } else if (points.size() == 2) {
            points.set(1, points.get(1).subtract(points.get(0)));
            return new MonomialCurve(points);
        } else if (points.size() == 3) {
            Vector3 c0 = points.get(0);
            Vector3 c1 = points.get(1).multiply(4);
            c1 = c1.subtract(points.get(0).multiply(3));
            c1 = c1.subtract(points.get(2));
            Vector3 c2 = points.get(2).subtract(points.get(0));
            c2 = c2.subtract(c1);

            ArrayList<Vector3> controlPoints = new ArrayList<>();
            controlPoints.add(c0);
            controlPoints.add(c1);
            controlPoints.add(c2);

            return new MonomialCurve(controlPoints);
        }
        return new MonomialCurve(points);
    }

    @Override
    public Vector3 derivative(double val) {
        if (val < 0 || val > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1, but was " + val);
        }

        Vector3 res = new Vector3();

        for (int i = 0; i < controlPoints.size(); i++) {
            double base = i * Math.pow(val, i - 1);
            //System.out.println("base: "+base+"  buffer: " +controlPoints.get(i).multiply(base) +"\n------------");
            Vector3 buffer = controlPoints.get(i).multiply(base);
            res = res.add(buffer);
        }
        //System.out.println("res: "+res);
        return res;
    }

    @Override
    public Vector3 evaluate(double val) {
        if (val < 0 || val > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1, but was " + val);
        }

        Vector3 res = new Vector3();

        for (int i = 0; i < controlPoints.size(); i++) {
            double base = Math.pow(val, i);
            //System.out.println("base: " + base + "  buffer: " + controlPoints.get(i).multiply(base) + " i: " + i);
            Vector3 ci = controlPoints.get(i);
            res = res.add(ci.multiply(base));
        }
        System.out.println("");
        return res;
    }
}
