import computergraphics.math.MonomialCurve;
import computergraphics.math.Vector3;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Eric on 16.12.2015.
 */
public class CurveTests {
    private ArrayList<Vector3> points1;
    private ArrayList<Vector3> points2;
    private ArrayList<Vector3> points3;

    private MonomialCurve m1;
    private MonomialCurve m2;
    private MonomialCurve m3;

    @Before
    public void init() {
        points1 = new ArrayList<>();
        points2 = new ArrayList<>();
        points3 = new ArrayList<>();
        points1.add(new Vector3(0, 0, 0));
        points1.add(new Vector3(0, 2, 0));
        points1.add(new Vector3(2, 2, 0));

        points2.add(new Vector3(1, 1, 1));
        points2.add(new Vector3(1, 3, 1));
        points2.add(new Vector3(1, 1, 5));

        points3.add(new Vector3(212, 3, 1));
        points3.add(new Vector3(2, 332, 1));
        points3.add(new Vector3(2, 3, 15));

        m1 = MonomialCurve.interpolate(points1);
        m2 = MonomialCurve.interpolate(points2);
        m3 = MonomialCurve.interpolate(points3);
    }

    @Test
    public void testCurveByCheckInterpolatedValues() {
        assertEquals("incorrect", new Vector3(0, 0, 0), m1.evaluate(0));
        assertEquals("incorrect", new Vector3(0, 2, 0), m1.evaluate(0.5));
        assertEquals("incorrect", new Vector3(2, 2, 0), m1.evaluate(1));

        assertEquals("incorrect", new Vector3(1, 1, 1), m2.evaluate(0));
        assertEquals("incorrect", new Vector3(1, 3, 1), m2.evaluate(0.5));
        assertEquals("incorrect", new Vector3(1, 1, 5), m2.evaluate(1));

        assertEquals("incorrect", new Vector3(212, 3, 1), m3.evaluate(0));
        assertEquals("incorrect", new Vector3(2, 332, 1), m3.evaluate(0.5));
        assertEquals("incorrect", new Vector3(2, 3, 15), m3.evaluate(1));
    }

}
