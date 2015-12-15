package computergraphics.math;

import java.util.ArrayList;

/**
 * Created by Eric on 13.12.2015.
 */
public abstract class AbstractCurve {
    protected ArrayList<Vector3> controlPoints;

    public abstract Vector3 derivative(double val);

    public abstract Vector3 evaluate(double val);
}
