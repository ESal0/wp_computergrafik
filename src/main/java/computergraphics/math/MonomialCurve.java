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

    @Override
    public Vector3 derivative(double val) {
        return null;
    }

    @Override
    public Vector3 evaluate(double val) {
        if (val < 0 && val > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1, but was " + val);
        }

        Vector3 res = new Vector3();

        for (int i = 0; i < controlPoints.size(); i++) {
            double base = Math.pow(val, i);
            System.out.println(base);
            res.add(controlPoints.get(i).multiply(base));

        }
        return res;
    }
}
