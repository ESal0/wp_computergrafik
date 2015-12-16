package computergraphics.math;

import java.util.ArrayList;

/**
 * Created by Eric on 15.12.2015.
 */
public class HermiteCurve extends AbstractCurve {


    public HermiteCurve(ArrayList<Vector3> points) {
        this.controlPoints = points;
    }

    @Override
    public Vector3 derivative(double val) {
        if (val < 0 || val > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1, but was " + val);
        }

        Vector3 res = new Vector3();

        for (int i = 0; i < controlPoints.size(); i++) {
            double base = evalDerivativeBaseFunction(val, i);
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
            double base = evalBaseFunction(val, i);
            //System.out.println("base: "+base+"  buffer: " +controlPoints.get(i).multiply(base) +"\n------------");
            Vector3 buffer = controlPoints.get(i).multiply(base);
            res = res.add(buffer);
        }
        //System.out.println("res: "+res);
        return res;
    }

    private double evalBaseFunction(double val, int i) {
        double res = 0.0;
        switch (i) {
            case 0:
                res = Math.pow((1 - val), 2) * (1 + 2 * val);
                break;
            case 1:
                res = val * Math.pow((1 - val), 2);
                break;
            case 2:
                res = -Math.pow(val, 2) * (1 - val);
                break;
            case 3:
                res = (3 - 2 * val) * Math.pow(val, 2);
                break;
            default:
                break;
        }
        return res;
    }

    private double evalDerivativeBaseFunction(double val, int i) {
        double res = 0.0;
        switch (i) {
            case 0:
                res = 6 * (val - 1) * val;
                break;
            case 1:
                res = 3 * Math.pow(val, 2) - 4 * val + 1;
                break;
            case 2:
                res = val * (3 * val - 2);
                break;
            case 3:
                res = -6 * (val - 1) * val;
                break;
            default:
                break;
        }
        return res;
    }

}
