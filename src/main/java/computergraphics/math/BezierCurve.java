package computergraphics.math;

import java.util.ArrayList;

/**
 * Created by abq331 on 16.12.2015.
 */
public class BezierCurve extends AbstractCurve {
    public BezierCurve(ArrayList<Vector3> v) {
        this.controlPoints = v;
    }

    @Override
    public Vector3 derivative(double val) {
        return null;
    }

    @Override
    public Vector3 evaluate(double val) {
        if (val < 0 || val > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1, but was " + val);
        }

        Vector3 res = new Vector3();

        for (int i = 0; i < controlPoints.size(); i++) {
            double base = calculateBase(val, i);
            System.out.println("base: " + base + "  buffer: " + controlPoints.get(i).multiply(base) + " i: " + i);
            Vector3 ci = controlPoints.get(i);
            res = res.add(ci.multiply(base));
        }
        System.out.println("");
        return res;
    }

    private double calculateBase(double val, int i) {
        double res = 0.0;
        int degree = controlPoints.size()-1;
        res = MathHelpers.factorial(degree) / (MathHelpers.factorial(i) * MathHelpers.factorial(degree - i));
        res *= Math.pow(val, i) * Math.pow((1 - val), degree - i);

        return res;
    }
}
